package com.team01.web.virtualwallet.services.contracts;

import com.team01.web.virtualwallet.models.Transaction;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.FilterTransactionByAdminParams;
import com.team01.web.virtualwallet.models.dto.FilterTransactionsByUserParams;

import java.util.List;

public interface TransactionService extends BaseGetService<Transaction> {

    List<Transaction> getUserTransactions(User user);

    List<Transaction>  getUserLatestTransactions(User user);

    List<Transaction> adminFilterTransactions(FilterTransactionByAdminParams params);

    List<Transaction> userFilterTransactions(FilterTransactionsByUserParams params);

    void create(Transaction transaction, User executor);

    void createLargeTransaction(Transaction transaction, String token, User executor);
}
