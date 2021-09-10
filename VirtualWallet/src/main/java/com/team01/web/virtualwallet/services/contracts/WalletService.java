package com.team01.web.virtualwallet.services.contracts;

import com.team01.web.virtualwallet.models.Wallet;

import java.util.List;

public interface WalletService {

    List<Wallet> getAll();

    Wallet getById(int id);

    Wallet create(Wallet wallet);
}
