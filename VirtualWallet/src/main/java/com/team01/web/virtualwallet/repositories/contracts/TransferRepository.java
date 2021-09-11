package com.team01.web.virtualwallet.repositories.contracts;

import com.team01.web.virtualwallet.models.Transfer;

import java.util.List;

public interface TransferRepository {
    List<Transfer> getAll();

    Transfer getById(int id);

    void create(Transfer transfer);
}
