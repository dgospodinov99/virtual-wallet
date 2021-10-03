package com.team01.web.virtualwallet.repositories.contracts;

import com.team01.web.virtualwallet.models.Transaction;
import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.models.dto.FilterTransactionByAdminParams;
import com.team01.web.virtualwallet.models.dto.FilterTransactionsByUserParams;

import java.util.List;

public interface TransactionRepository extends BaseGetRepository<Transaction> {

    List<Transaction> getAllWalletTransactions(Wallet wallet);

    List<Transaction> getWalletLatestTransactions(Wallet wallet);

    //todo CreatableRepository interface?
    void create(Transaction transaction);

    List<Transaction> adminFilterTransactions(FilterTransactionByAdminParams params);

    List<Transaction> userFilterTransactions(FilterTransactionsByUserParams params);
}
