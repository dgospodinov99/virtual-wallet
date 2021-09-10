package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.repositories.contracts.TransferRepository;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    @Override
    public List<Transfer> getAll() {
        return transferRepository.getAll();
    }

    @Override
    public Transfer getById(int id) {
        return transferRepository.getById(id);
    }
}
