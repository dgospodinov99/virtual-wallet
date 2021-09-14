package com.team01.web.virtualwallet.repositories.contracts;

import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.Wallet;

import java.util.List;

public interface TransferRepository {
    List<Transfer> getAll();

    Transfer getById(int id);

    List<Transfer> getAllWalletTransfers(Wallet wallet);

    void create(Transfer transfer);
}
