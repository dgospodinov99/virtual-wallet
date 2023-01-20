package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.*;
import com.team01.web.virtualwallet.models.Transaction;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.models.dto.FilterTransactionByAdminParams;
import com.team01.web.virtualwallet.models.dto.FilterTransactionsByUserParams;
import com.team01.web.virtualwallet.repositories.contracts.TransactionRepository;
import com.team01.web.virtualwallet.services.contracts.TransactionService;
import com.team01.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl extends BaseGetServiceImpl<Transaction> implements TransactionService {

    private static final String USER_BLOCKED_MESSAGE = "You are blocked from making transactions!";
    private static final String USER_AND_WALLET_DONT_MATCH_ERROR = "Users can make transfers only from their own or shared wallet!";
    private static final double LARGE_TRANSACTION_AMOUNT = 1000;
    private static final String LARGE_TRANSACTION_MESSAGE = "A verification code has been sent to your email to verify large transaction.";
    private static final String BALANCE_IS_NOT_ENOUGH = "Balance is not enough";

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, WalletService walletService) {
        super(transactionRepository);
        this.transactionRepository = transactionRepository;
        this.walletService = walletService;
    }

    @Override
    public List<Transaction> getUserTransactions(User user, User executor) {
        validateUser(executor, user.getWallet());
        return transactionRepository.getAllWalletTransactions(user.getWallet());
    }

    @Override
    public List<Transaction> getUserLatestTransactions(User user) {
        return transactionRepository.getWalletLatestTransactions(user.getWallet());
    }

    @Override
    public List<Transaction> adminFilterTransactions(FilterTransactionByAdminParams params) {
        return transactionRepository.adminFilterTransactions(params);
    }

    @Override
    public List<Transaction> userFilterTransactions(FilterTransactionsByUserParams params) {
        return transactionRepository.userFilterTransactions(params);
    }


    @Override
    public void create(Transaction transaction, User executor) {
        validateUser(executor, transaction.getSender());
        validateUserNotBlocked(executor);
        validateBalance(transaction.getSender(), transaction.getAmount());
        validateTransaction(executor.getWallet(), transaction.getReceiver());
        walletService.deposit(transaction.getReceiver(), transaction.getAmount()); //add money to receiver
        walletService.withdraw(transaction.getSender(), transaction.getAmount());  //remove money from sender

        transactionRepository.create(transaction);
    }

    @Override
    public void checkForLargeTransaction(Transaction transaction) {
        if (transaction.getAmount() > LARGE_TRANSACTION_AMOUNT) {
            throw new LargeTransactionDetectedException(LARGE_TRANSACTION_MESSAGE);
        }
    }

    private void validateBalance(Wallet sender, double amount) {
        if (amount > sender.getBalance()) {
            throw new InvalidTransferException(BALANCE_IS_NOT_ENOUGH);
        }
    }

    private void validateUserNotBlocked(User user) {
        if (user.isBlocked()) {
            throw new BlockedUserException(USER_BLOCKED_MESSAGE);
        }
    }

    private void validateUser(User executor, Wallet wallet) {
        if (!executor.isAdmin() && executor.getWallet().getId() != wallet.getId()) {
            throw new UnauthorizedOperationException(USER_AND_WALLET_DONT_MATCH_ERROR);
        }
    }

    private void validateTransaction(Wallet sender, Wallet receiver) {
        if (sender.getId() == receiver.getId()) {
            throw new InvalidUserInput("You can't transfer money to yourself :)");
        }
    }
}
