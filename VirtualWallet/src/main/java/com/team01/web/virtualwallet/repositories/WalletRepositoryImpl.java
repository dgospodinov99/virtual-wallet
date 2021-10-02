package com.team01.web.virtualwallet.repositories;

import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.repositories.contracts.WalletRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WalletRepositoryImpl extends BaseModifyRepositoryImpl<Wallet> implements WalletRepository {

    @Autowired
    public WalletRepositoryImpl(SessionFactory sessionFactory) {
        super(Wallet.class, sessionFactory);
    }
}
