package com.team01.web.virtualwallet.repositories;

import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.repositories.contracts.WalletRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WalletRepositoryImpl extends BaseGetRepositoryImpl<Wallet> implements WalletRepository {

    @Autowired
    public WalletRepositoryImpl(SessionFactory sessionFactory) {
        super(Wallet.class, sessionFactory);
    }

    @Override
    public void update(Wallet wallet) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(wallet);
            session.getTransaction().commit();
        }
    }

    @Override
    public void create(Wallet wallet) {
        try (Session session = getSessionFactory().openSession()) {
            session.save(wallet);
        }
    }

    @Override
    public void delete(Wallet wallet) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(wallet);
            session.getTransaction().commit();
        }
    }
}
