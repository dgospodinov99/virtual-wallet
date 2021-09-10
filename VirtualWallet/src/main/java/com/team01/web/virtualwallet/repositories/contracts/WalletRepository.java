package com.team01.web.virtualwallet.repositories.contracts;

import com.team01.web.virtualwallet.models.Wallet;

import java.util.List;

public interface WalletRepository {

    List<Wallet> getAll();

    Wallet getById(int id);

    void create(Wallet wallet);
}
