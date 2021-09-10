package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.DuplicateEntityException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.exceptions.InvalidPasswordException;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.repositories.contracts.UserRepository;
import com.team01.web.virtualwallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public void create(User user) {
        boolean duplicateUsernameExists = true;
        boolean duplicateEmailExists = true;
        try {
            userRepository.getByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateUsernameExists = false;
        }
        try {
            userRepository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateEmailExists = false;
        }
        if (duplicateUsernameExists) {
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }
        if (duplicateEmailExists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
        isPasswordValid(user.getPassword());
        userRepository.create(user);
    }

    @Override
    public void update(User user) {
        boolean duplicateEmailExists = true;
        try {
            User existingUser = userRepository.getByEmail(user.getEmail());
            if (existingUser.getId() == user.getId()) {
                duplicateEmailExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateEmailExists = false;
        }
        if (duplicateEmailExists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
        isPasswordValid(user.getPassword());
        userRepository.update(user);
    }

    @Override
    public void delete(int id) {
        userRepository.delete(id);
    }

    public void isPasswordValid(String password) {
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
}
