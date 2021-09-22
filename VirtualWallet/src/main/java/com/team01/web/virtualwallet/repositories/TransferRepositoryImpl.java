package com.team01.web.virtualwallet.repositories;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.Transaction;
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
public class TransferRepositoryImpl implements TransferRepository {

    private static final int LATEST_TRANSFER_SIZE = 5;

    private final SessionFactory sessionFactory;

    @Autowired
    public TransferRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Transfer> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Transfer> query = session.createQuery("from Transfer order by id", Transfer.class);
            return query.list();
        }
    }

    @Override
    public Transfer getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transfer transfer = session.get(Transfer.class, id);
            if (transfer == null) {
                throw new EntityNotFoundException("Transaction", id);
            }
            return transfer;
        }
    }

    @Override
    public List<Transfer> getWalletTransfers(Wallet wallet) {
        try (Session session = sessionFactory.openSession()) {
            Query<Transfer> query = session.createQuery("from Transfer where wallet.id = :walletId", Transfer.class);
            query.setParameter("walletId", wallet.getId());
            return query.list();
        }
    }

    @Override
    public List<Transfer> getLatestWalletTransfers(Wallet wallet) {
        try (Session session = sessionFactory.openSession()) {
            Query<Transfer> query = session.createQuery("from Transfer where wallet.id = :walletId order by timestamp desc", Transfer.class);
            query.setParameter("walletId", wallet.getId());
            query.setMaxResults(LATEST_TRANSFER_SIZE);
            return query.list();
        }
    }

    @Override
    public void create(Transfer transfer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(transfer);
            session.getTransaction().commit();
        }
    }


}
