package com.team01.web.virtualwallet.repositories;

import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.repositories.contracts.TransferRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransferRepositoryImpl extends BaseModifyRepositoryImpl<Transfer> implements TransferRepository {

    private static final int LATEST_TRANSFER_SIZE = 5;

    @Autowired
    public TransferRepositoryImpl(SessionFactory sessionFactory) {
        super(Transfer.class, sessionFactory);
    }

    @Override
    public List<Transfer> getWalletTransfers(Wallet wallet) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Transfer> query = session.createQuery("from Transfer where wallet.id = :walletId", Transfer.class);
            query.setParameter("walletId", wallet.getId());
            return query.list();
        }
    }

    @Override
    public List<Transfer> getLatestWalletTransfers(Wallet wallet) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Transfer> query = session.createQuery("from Transfer where wallet.id = :walletId order by timestamp desc", Transfer.class);
            query.setParameter("walletId", wallet.getId());
            query.setMaxResults(LATEST_TRANSFER_SIZE);
            return query.list();
        }
    }
}
