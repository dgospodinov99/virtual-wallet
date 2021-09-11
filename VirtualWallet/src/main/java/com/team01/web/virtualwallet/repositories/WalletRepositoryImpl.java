package com.team01.web.virtualwallet.repositories;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.repositories.contracts.WalletRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WalletRepositoryImpl implements WalletRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public WalletRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Wallet> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Wallet> query = session.createQuery("from Wallet order by id", Wallet.class);
            return query.list();
        }
    }

    @Override
    public Wallet getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Wallet wallet = session.get(Wallet.class, id);
            if (wallet == null) {
                throw new EntityNotFoundException("Wallet", id);
            }
            return wallet;
        }
    }

    @Override
    public void update(Wallet wallet) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(wallet);
            session.getTransaction().commit();
        }
    }

    @Override
    public void create(Wallet wallet) {
        try (Session session = sessionFactory.openSession()) {
            session.save(wallet);
        }
    }
}
