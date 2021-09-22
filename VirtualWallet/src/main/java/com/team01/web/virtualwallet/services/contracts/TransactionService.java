package com.team01.web.virtualwallet.services.contracts;

import com.team01.web.virtualwallet.models.Transaction;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.FilterTransactionParams;

import java.util.List;

public interface TransactionService {

    List<Transaction> getAll();

    Transaction getById(int id);

    List<Transaction> getUserTransactions(User user);

    List<Transaction>  getUserLatestTransactions(User user);

    List<Transaction> filterTransactions(FilterTransactionParams params);

    void create(Transaction transaction, User executor);
}
