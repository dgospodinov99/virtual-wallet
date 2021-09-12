package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.BlockedUserException;
import com.team01.web.virtualwallet.exceptions.InvalidTransferException;
import com.team01.web.virtualwallet.exceptions.UnauthorizedOperationException;
import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.repositories.contracts.TransferRepository;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

    private static final String USER_BLOCKED_MESSAGE = "You are blocked from making transactions!";
    private static final String USER_AND_WALLET_DONT_MATCH_ERROR = "Users can make transfers only from their own or shared wallet!";

    private final TransferRepository transferRepository;
    private final WalletService walletService;
    private final UserService userService;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository, WalletService walletService, UserService userService) {
        this.transferRepository = transferRepository;
        this.walletService = walletService;
        this.userService = userService;
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
    public void create(Transfer transfer, User executor) {
        //connect to dummy api?
        validateUser(executor, transfer.getSender());
        validateUserStatus(transfer.getSender());
        validateTransfer(transfer.getSender(),transfer.getAmount());

        walletService.deposit(transfer.getReceiver(),transfer.getAmount()); //add money to receiver
        walletService.withdraw(transfer.getSender(),transfer.getAmount());  //remove money from sender

        transferRepository.create(transfer);
    }

    private void validateTransfer(Wallet sender, double amount){
        if(amount > sender.getBalance()){
            throw new InvalidTransferException("Balance is not enough");
        }
    }

    private void validateUserStatus(Wallet sender){
        if(userService.getByWallet(sender).isBlocked()){
            throw new BlockedUserException(USER_BLOCKED_MESSAGE);
        }
    }
    private void validateUser(User executor, Wallet wallet){
        if(!executor.isAdmin() && executor.getWallet().getId() != wallet.getId()){
            throw new InvalidTransferException(USER_AND_WALLET_DONT_MATCH_ERROR);
        }
    }

}
