package com.team01.web.virtualwallet.services.utils;

import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.dto.TransferDto;
import com.team01.web.virtualwallet.services.contracts.CardService;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import com.team01.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        return transfer;
    }

    public TransferDto toDto(Transfer transfer) {
        TransferDto dto = new TransferDto();
        dto.setAmount(transfer.getAmount());
        dto.setCardId(transfer.getCard().getId());
        dto.setWalletId(transfer.getWallet().getId());

        return dto;
    }
}
