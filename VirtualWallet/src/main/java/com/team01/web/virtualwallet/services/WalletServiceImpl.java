package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.repositories.contracts.WalletRepository;
import com.team01.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletServiceImpl extends BaseGetServiceImpl<Wallet> implements WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository) {
        super(walletRepository);
        this.walletRepository = walletRepository;
    }

    @Override
    public void update(Wallet wallet) {
        walletRepository.update(wallet);
    }

    @Override
    public void withdraw(Wallet wallet, double amount) {
        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.update(wallet);
    }

    @Override
    public void deposit(Wallet wallet, double amount) {
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.update(wallet);
    }

    @Override
    public Wallet create(Wallet wallet) {
        walletRepository.create(wallet);
        return wallet;
    }

    @Override
    public void delete(Wallet wallet) {
        wallet.setActive(false);
        walletRepository.update(wallet);
    }
}
