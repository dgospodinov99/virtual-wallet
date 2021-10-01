package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.*;
import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.Transaction;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.models.dto.ChangePasswordDto;
import com.team01.web.virtualwallet.models.dto.FilterUserParams;
import com.team01.web.virtualwallet.repositories.contracts.UserRepository;
import com.team01.web.virtualwallet.services.contracts.CardService;
import com.team01.web.virtualwallet.services.contracts.TransactionService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_ADMIN_MESSAGE = "You are not an administrator!";
    private static final String USER_AND_WALLET_DONT_MATCH = "You can only list your own transactions!";

    private final UserRepository userRepository;
    private final WalletService walletService;
    private final CardService cardService;
    private final TransactionService transactionService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, WalletService walletService, CardService cardService, TransactionService transactionService) {
        this.userRepository = userRepository;
        this.walletService = walletService;
        this.cardService = cardService;

        this.transactionService = transactionService;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public User getByWallet(Wallet wallet) {
        return userRepository.getByWallet(wallet.getId());
    }

    @Override
    public User blockUserByAdmin(String usernameToBlock, User executor) {
        User userToBlock = getByUsername(usernameToBlock);

        verifyAdmin(executor);

        userToBlock.setBlocked(true);
        userRepository.update(userToBlock);
        return userToBlock;
    }

    @Override
    public User unblockUserByAdmin(String usernameToUnBlock, User executor) {
        User userToUnBlock = getByUsername(usernameToUnBlock);

        verifyAdmin(executor);

        userToUnBlock.setBlocked(false);
        userRepository.update(userToUnBlock);
        return userToUnBlock;
    }

    @Override
    public void blockUserOnRegistration(User user) {
        user.setBlocked(true);
        userRepository.update(user);
    }

    @Override
    public void unblockUserOnRegistration(User user) {
        user.setBlocked(false);
        userRepository.update(user);
    }

    private void verifyAdmin(User executor) {
        if (!executor.isAdmin()) {
            throw new UnauthorizedOperationException(USER_NOT_ADMIN_MESSAGE);
        }
    }

    @Override
    public List<User> filterUsers(FilterUserParams params) {
        return userRepository.filterUsers(params);
    }

    @Override
    public List<Transaction> getUserTransactions(int id, User executor) {
        User user = getById(id);
        validateUser(executor,user.getWallet());

        return transactionService.getUserTransactions(user);
    }

    @Override
    public List<Transaction> getUserLatestTransactions(User executor) {
        return transactionService.getUserLatestTransactions(executor);
    }

    @Override
    public User search(String searchItem) {
        return userRepository.search(searchItem);
    }

    @Override
    public void create(User user) {
        verifyUniqueEmail(user.getEmail(),user);
        verifyUniqueUsername(user.getUsername(),user);
        verifyUniquePhoneNumber(user.getPhoneNumber(), user);
        isPasswordValid(user.getPassword());
        Wallet wallet = walletService.create(new Wallet());
        user.setWallet(wallet);
        userRepository.create(user);
    }

    @Override
    public void update(User user) {
        verifyUniqueEmail(user.getEmail(),user);
        verifyUniquePhoneNumber(user.getPhoneNumber(), user);
        isPasswordValid(user.getPassword());
        userRepository.update(user);
    }

    @Override
    public void delete(int id) {
        User user = getById(id);
        //card deletion
        for (Card card : user.getCards()) {
            cardService.delete(card.getId(), user);
        }

        walletService.delete(user.getWallet());
        user.setActive(false);
        userRepository.update(user);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        isPasswordValid(newPassword);
        user.setPassword(newPassword);
        userRepository.update(user);
    }

    private void isPasswordValid(String password) {
        Pattern specialSymbolPattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern upperCasePattern = Pattern.compile("[A-Z ]");
        Pattern lowerCasePattern = Pattern.compile("[a-z ]");
        Pattern digitPattern = Pattern.compile("[0-9 ]");

        if (password.length() < 8) {
            throw new InvalidPasswordException("Password must be at least 8 symbols long!");
        }
        if (!specialSymbolPattern.matcher(password).find()) {
            throw new InvalidPasswordException("Password must contain a special symbol!");
        }
        if (!upperCasePattern.matcher(password).find()) {
            throw new InvalidPasswordException("Password must contain an upper-case letter!");
        }
        if (!lowerCasePattern.matcher(password).find()) {
            throw new InvalidPasswordException("Password must contain a lower-case letter!");
        }
        if (!digitPattern.matcher(password).find()) {
            throw new InvalidPasswordException("Password must contain a number!");
        }
    }

    private void verifyUniqueEmail(String email, User user) {
        boolean duplicateExists = true;
        try {
            User existingUser = userRepository.getByEmail(email);
            if(existingUser.getId() == user.getId()){
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("User", "email", email);
        }
    }

    private void verifyUniqueUsername(String username, User user) {
        boolean duplicateExists = true;
        try {
            User existingUser = userRepository.getByUsername(username);
            if(existingUser.getId() == user.getId()){
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("User", "username", username);
        }
    }

    private void verifyUniquePhoneNumber(String phoneNumber, User user) {
        boolean duplicateExists = true;
        try {
            User existingUser = userRepository.getByPhoneNumber(phoneNumber);
            if(existingUser.getId() == user.getId()){
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("User", "phone number", phoneNumber);
        }
    }

    protected void validateUser(User executor,Wallet wallet) {
        if (!executor.isAdmin() && executor.getWallet().getId() != wallet.getId()) {
            throw new UnauthorizedOperationException(USER_AND_WALLET_DONT_MATCH);
        }
    }
}
