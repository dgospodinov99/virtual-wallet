package com.team01.web.virtualwallet.services.contracts;

import com.team01.web.virtualwallet.models.Wallet;

import java.util.List;

public interface WalletService extends BaseGetService<Wallet> {

    void update(Wallet wallet);

    void withdraw(Wallet wallet, double amount);

    void deposit(Wallet wallet, double amount);

    Wallet create(Wallet wallet);

    void delete(Wallet wallet);
}
