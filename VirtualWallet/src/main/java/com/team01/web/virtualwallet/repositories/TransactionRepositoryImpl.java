package com.team01.web.virtualwallet.repositories;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.Transaction;
import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.models.dto.FilterTransactionParams;
import com.team01.web.virtualwallet.repositories.contracts.TransactionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TransactionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Transaction> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Transaction> query = session.createQuery("from Transaction order by id", Transaction.class);
            return query.list();
        }
    }

    @Override
    public Transaction getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.get(Transaction.class, id);
            if (transaction == null) {
                throw new EntityNotFoundException("Transaction", id);
            }
            return transaction;
        }
    }

    @Override
    public List<Transaction> filterTransactions(FilterTransactionParams params) {
        try (Session session = sessionFactory.openSession()) {
            String queryString = "from Transaction where 1 = 1";

            if (params.getReceiverUsername().isPresent()) {
                queryString += " and receiver.id = :receiverId";
            }
            if (params.getSenderUsername().isPresent()) {
                queryString += " and sender.id = :senderId";
            }
//            if (params.getDirection().isPresent()) {
//                if(params.getDirection().equals(TransferDirection.IN)){
//                    queryString += " and receiver = :?";
//                }else {
//                    queryString += " and sender = :?";
//                }
//            }
//            if(params.getSortParam().isPresent()){
//                if(params.getSortParam().equals("amount")){
//                    queryString += " order by amount desc";
//                }
//            }

            // + sort param
            Query<Transaction> query = session.createQuery(queryString, Transaction.class);

            if (params.getReceiverUsername().isPresent()) {
                query.setParameter("receiverId", params.getReceiverUsername().orElse(null));
            }
            if (params.getSenderUsername().isPresent()) {
                query.setParameter("senderId", params.getSenderUsername().orElse(null));
            }

            return query.list();
        }
    }

    @Override
    public List<Transaction> getAllWalletTransactions(Wallet wallet) {
        try (Session session = sessionFactory.openSession()) {
            Query<Transaction> query = session.createQuery("from Transaction where sender.id = :walletId or receiver.id = :walletId", Transaction.class);
            query.setParameter("walletId", wallet.getId());
            return query.list();
        }
    }

    @Override
    public void create(Transaction transaction) {
        try (Session session = sessionFactory.openSession()) {
            session.save(transaction);
        }
    }


}
