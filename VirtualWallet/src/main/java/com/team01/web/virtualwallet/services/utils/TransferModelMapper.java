package com.team01.web.virtualwallet.services.utils;

import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.models.dto.DepositDto;
import com.team01.web.virtualwallet.models.dto.DummyDto;
import com.team01.web.virtualwallet.models.dto.TransferDto;
import com.team01.web.virtualwallet.services.contracts.CardService;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import com.team01.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TransferModelMapper {

    private final TransferService transferService;
    private final CardService cardService;
    private final WalletService walletService;

    @Autowired
    public TransferModelMapper(TransferService transferService, CardService cardService, WalletService walletService) {
        this.transferService = transferService;
        this.cardService = cardService;
        this.walletService = walletService;
    }


    public Transfer fromDto(TransferDto dto) {
        Transfer transfer = new Transfer();
        transfer.setAmount(dto.getAmount());
        transfer.setCard(cardService.getById(dto.getCardId()));
        transfer.setWallet(walletService.getById(dto.getWalletId()));
        transfer.setTimestamp(LocalDateTime.now());

        return transfer;
    }

    public Transfer fromDto(DepositDto dto, Wallet wallet) {
        Transfer transfer = new Transfer();
        transfer.setAmount(dto.getAmount());
        transfer.setCard(cardService.getById(dto.getCardId()));
        transfer.setWallet(wallet);
        transfer.setTimestamp(LocalDateTime.now());

        return transfer;
    }

    public TransferDto toDto(Transfer transfer) {
        TransferDto dto = new TransferDto();
        dto.setAmount(transfer.getAmount());
        dto.setCardId(transfer.getCard().getId());
        dto.setWalletId(transfer.getWallet().getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        String date = transfer.getTimestamp().format(formatter);

        dto.setTimestamp(date);

        return dto;
    }

    public DummyDto transferToDummyDto(Transfer transfer) {
        DummyDto dummyDto = new DummyDto();
        dummyDto.setAmount(transfer.getAmount());
        dummyDto.setCardCheck(transfer.getCard().getCheckNumber());
        dummyDto.setExpirationDate(String.valueOf(transfer.getCard().getExpirationDate()));
        dummyDto.setCardNumber(transfer.getCard().getCardNumber());
        return dummyDto;
    }
}
