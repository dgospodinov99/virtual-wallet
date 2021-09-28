package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.BadLuckException;
import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.repositories.contracts.TransferRepository;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import com.team01.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final WalletService walletService;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository, WalletService walletService) {
        this.transferRepository = transferRepository;
        this.walletService = walletService;
    }

    @Override
    public List<Transfer> getAll() {
        return transferRepository.getAll();
    }

    @Override
    public List<Transfer> getUserTransfers(User user) {
        return transferRepository.getWalletTransfers(user.getWallet());
    }

    @Override
    public List<Transfer> getUserLatestTransfers(User user) {
        return transferRepository.getLatestWalletTransfers(user.getWallet());
    }

    @Override
    public Transfer getById(int id) {
        return transferRepository.getById(id);
    }

    @Override
    public void create(Transfer transfer) {
        transferRepository.create(transfer);
        walletService.deposit(transfer.getWallet(), transfer.getAmount());
    }

}
