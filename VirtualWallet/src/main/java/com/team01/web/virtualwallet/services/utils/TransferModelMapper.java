package com.team01.web.virtualwallet.services.utils;

import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.dto.TransferDto;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferModelMapper {

    private final TransferService transferService;
    private final UserService userService;
    private final WalletService walletService;

    @Autowired
    public TransferModelMapper(TransferService transferService, UserService userService, WalletService walletService) {
        this.transferService = transferService;
        this.userService = userService;
        this.walletService = walletService;
    }

    public Transfer fromDto(TransferDto dto) {
        Transfer transfer = new Transfer();
        transfer.setAmount(dto.getAmount());
        transfer.setSender(walletService.getById(dto.getSenderId()));
        transfer.setReceiver(walletService.getById(dto.getReceiverId()));

        return transfer;
    }

    public TransferDto toDto(Transfer transfer) {
        TransferDto dto = new TransferDto();
        dto.setAmount(transfer.getAmount());
        dto.setSenderId(transfer.getSender().getId());
        dto.setReceiverId(transfer.getReceiver().getId());

        return dto;
    }
}
