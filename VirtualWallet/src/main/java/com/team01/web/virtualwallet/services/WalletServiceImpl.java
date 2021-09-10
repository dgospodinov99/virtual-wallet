package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.repositories.contracts.WalletRepository;
import com.team01.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    @Override
    public List<Wallet> getAll() {
        return walletRepository.getAll();
    }

    @Override
    public Wallet getById(int id) {
        return walletRepository.getById(id);
    }

    @Override
    public Wallet create(Wallet wallet) {
       walletRepository.create(wallet);
       return wallet;
    }
}
