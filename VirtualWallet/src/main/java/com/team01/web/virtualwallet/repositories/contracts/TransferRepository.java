package com.team01.web.virtualwallet.repositories.contracts;

import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.Wallet;

import java.util.List;

public interface TransferRepository extends BaseGetRepository<Transfer>{

    //todo
    void create(Transfer transfer);

    List<Transfer> getWalletTransfers(Wallet wallet);

    List<Transfer> getLatestWalletTransfers(Wallet wallet);

}
