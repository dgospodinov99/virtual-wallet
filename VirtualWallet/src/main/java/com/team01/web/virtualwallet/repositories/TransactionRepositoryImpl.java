package com.team01.web.virtualwallet.repositories;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.Transaction;
import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.models.dto.FilterTransactionByAdminParams;
import com.team01.web.virtualwallet.models.dto.FilterTransactionsByUserParams;
import com.team01.web.virtualwallet.repositories.contracts.TransactionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepositoryImpl extends BaseGetRepositoryImpl<Transaction> implements TransactionRepository {

    private static final int LATEST_TRANSACTIONS_SIZE = 5;

    @Autowired
    public TransactionRepositoryImpl(SessionFactory sessionFactory) {
        super(Transaction.class, sessionFactory);
    }

    @Override
    public List<Transaction> adminFilterTransactions(FilterTransactionByAdminParams params) {
        try (Session session = getSessionFactory().openSession()) {
            String queryString = "from Transaction where 1 = 1";

            if (params.getReceiverId().isPresent()) {
                queryString += " and receiver.id = :receiverId";
            }
            if (params.getSenderId().isPresent()) {
                queryString += " and sender.id = :senderId";
            }

            if (params.getStartDate().isPresent() && params.getEndDate().isPresent()) {
                queryString += " and timestamp between :startDate and :endDate";
            } else if (params.getStartDate().isPresent()) {
                queryString += " and timestamp >= :startDate";
            } else if (params.getEndDate().isPresent()) {
                queryString += " and timestamp <= :endDate";
            }

            //sorting params
            if (params.getSortParam().isPresent()) {
                if (params.getSortParam().get().equals("amount")) {
                    queryString += " order by amount desc";
                } else if (params.getSortParam().get().equals("date")) {
                    queryString += " order by timestamp desc";
                }
            }

            Query<Transaction> query = session.createQuery(queryString, Transaction.class);

            if (params.getReceiverId().isPresent()) {
                query.setParameter("receiverId", params.getReceiverId().orElse(null));
            }
            if (params.getSenderId().isPresent()) {
                query.setParameter("senderId", params.getSenderId().orElse(null));
            }

            //set date params in query
            if (params.getStartDate().isPresent() && params.getEndDate().isPresent()) {
                query.setParameter("startDate", params.getStartDate().get());
                query.setParameter("endDate", params.getEndDate().get());
            } else if (params.getStartDate().isPresent()) {
                query.setParameter("startDate", params.getStartDate().get());
            } else if (params.getEndDate().isPresent()) {
                query.setParameter("endDate", params.getEndDate().get());
            }

            return query.list();
        }
    }

    @Override
    public List<Transaction> userFilterTransactions(FilterTransactionsByUserParams params) {

        try (Session session = getSessionFactory().openSession()) {
            String queryString = "from Transaction where 1 = 1";

            if (params.getDirection().isPresent()) {
                var direction = params.getDirection().get();
                switch (direction) {
                    case IN: {
                        queryString += " and receiver.id = :walletId";
                        break;
                    }
                    case OUT: {
                        queryString += " and sender.id = :walletId";
                        break;
                    }
                }
            }

            if (params.getStartDate().isPresent() && params.getEndDate().isPresent()) {
                queryString += " and timestamp between :startDate and :endDate";
            } else if (params.getStartDate().isPresent()) {
                queryString += " and timestamp >= :startDate";
            } else if (params.getEndDate().isPresent()) {
                queryString += " and timestamp <= :endDate";
            }


            Query<Transaction> query = session.createQuery(queryString, Transaction.class);

            //todo refactor maybe
            //set date params in query
            if (params.getStartDate().isPresent() && params.getEndDate().isPresent()) {
                query.setParameter("startDate", params.getStartDate().get());
                query.setParameter("endDate", params.getEndDate().get());
            } else if (params.getStartDate().isPresent()) {
                query.setParameter("startDate", params.getStartDate().get());
            } else if (params.getEndDate().isPresent()) {
                query.setParameter("endDate", params.getEndDate().get());
            }

            if (params.getDirection().isPresent()) {
                query.setParameter("walletId", params.getWalletId().get());
            }

            return query.list();
        }
    }

    @Override
    public List<Transaction> getAllWalletTransactions(Wallet wallet) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Transaction> query = session.createQuery("from Transaction where sender.id = :walletId or receiver.id = :walletId", Transaction.class);
            query.setParameter("walletId", wallet.getId());
            return query.list();
        }
    }

    @Override
    public List<Transaction> getWalletLatestTransactions(Wallet wallet) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Transaction> query = session.createQuery(
                    "from Transaction where sender.id = :walletId or receiver.id = :walletId order by timestamp desc", Transaction.class);
            query.setParameter("walletId", wallet.getId());
            query.setMaxResults(LATEST_TRANSACTIONS_SIZE);
            return query.list();
        }
    }


    @Override
    public void create(Transaction transaction) {
        try (Session session = getSessionFactory().openSession()) {
            session.save(transaction);
        }
    }


}
