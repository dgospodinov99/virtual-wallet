package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.exceptions.InvalidTransferException;
import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.repositories.contracts.TransferRepository;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import com.team01.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Transfer getById(int id) {
        return transferRepository.getById(id);
    }

    @Override
    public void create(Transfer transfer) {
        //connect to dummy api?
        //remove money from sender
        //add money to receiver
        validateTransfer(transfer.getSender(),transfer.getAmount());
        walletService.deposit(transfer.getReceiver(),transfer.getAmount());
        walletService.withdraw(transfer.getSender(),transfer.getAmount());
        transferRepository.create(transfer);
    }

    private void validateTransfer(Wallet sender, double amount){
        if(amount > sender.getBalance()){
            throw new InvalidTransferException("Balance is not enough");
        }
    }

}
