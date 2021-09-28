package com.team01.web.virtualwallet.services.utils;

import com.team01.web.virtualwallet.models.Transaction;
import com.team01.web.virtualwallet.models.dto.CreateTransactionDto;
import com.team01.web.virtualwallet.models.dto.TransactionDto;
import com.team01.web.virtualwallet.services.contracts.TransactionService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TransactionModelMapper {

    private final TransactionService transactionService;
    private final UserService userService;
    private final WalletService walletService;

    @Autowired
    public TransactionModelMapper(TransactionService transactionService, UserService userService, WalletService walletService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.walletService = walletService;
    }

    public Transaction fromDto(CreateTransactionDto dto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setReceiver(walletService.getById(dto.getReceiverId()));
        transaction.setTimestamp(LocalDateTime.now());

        return transaction;
    }

    public TransactionDto toDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setAmount(transaction.getAmount());
        dto.setSender(userService.getById(transaction.getSender().getId()).getUsername());
        dto.setReceiver(userService.getById(transaction.getReceiver().getId()).getUsername());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm  dd-MM-yyyy");
        String date = transaction.getTimestamp().format(formatter);
        dto.setTimestamp(date);
        return dto;
    }
}
