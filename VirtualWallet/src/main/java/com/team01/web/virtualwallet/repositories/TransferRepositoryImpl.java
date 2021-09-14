package com.team01.web.virtualwallet.repositories;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.User;
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
                throw new EntityNotFoundException("Transfer", id);
            }
            return transfer;
        }
    }

    @Override
    public List<Transfer> getAllWalletTransfers(Wallet wallet) {
        try (Session session = sessionFactory.openSession()) {
            Query<Transfer> query = session.createQuery("from Transfer where sender.id = :walletId or receiver.id = :walletId", Transfer.class);
            query.setParameter("walletId", wallet.getId());
            return query.list();
        }
    }

    @Override
    public void create(Transfer transfer) {
        try (Session session = sessionFactory.openSession()) {
            session.save(transfer);
        }
    }
}
