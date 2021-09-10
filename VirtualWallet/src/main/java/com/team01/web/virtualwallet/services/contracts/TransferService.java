package com.team01.web.virtualwallet.services.contracts;

import com.team01.web.virtualwallet.models.Transfer;

import java.util.List;

public interface TransferService {

    List<Transfer> getAll();

    Transfer getById(int id);
}
