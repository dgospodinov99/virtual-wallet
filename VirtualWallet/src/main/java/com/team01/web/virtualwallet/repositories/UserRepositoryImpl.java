package com.team01.web.virtualwallet.repositories;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.FilterUserParams;
import com.team01.web.virtualwallet.repositories.contracts.BaseGetRepository;
import com.team01.web.virtualwallet.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserRepositoryImpl extends BaseModifyRepositoryImpl<User> implements UserRepository {

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("User", "username", username);
            }
            return query.list().get(0);
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Session session = getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("User", "email", email);
            }
            return query.list().get(0);
        }
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        try (Session session = getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User where phoneNumber = :phoneNumber", User.class);
            query.setParameter("phoneNumber", phoneNumber);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("User", "phone number", phoneNumber);
            }
            return query.getSingleResult();
        }
    }

    @Override
    public User getByWallet(int id) {
        try (Session session = getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User where wallet.id = :id", User.class);
            query.setParameter("id", id);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("User", "Wallet-Id", String.valueOf(id));
            }
            return query.list().get(0);
        }
    }

    @Override
    public User search(String searchItem) {
        try (Session session = getSessionFactory().openSession()){
            Query<User> query = session.createQuery("from User where " +
                    "username like concat('%', :searchItem, '%') " +
                    "or email like concat('%', :searchItem, '%') " +
                    "or phoneNumber like concat('%', :searchItem, '%')");
            query.setParameter("searchItem", searchItem);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("User", "Property", searchItem);
            }
            return query.getSingleResult();
        }
    }

    @Override
    public List<User> filterUsers(FilterUserParams fup) {
        try (Session session = getSessionFactory().openSession()) {
            StringBuilder baseQuery = new StringBuilder().append("from User");
            var filters = new ArrayList<String>();
            var params = new HashMap<String, String>();

            fup.getUsername().ifPresent(value -> {
                filters.add("username like :username");
                params.put("username", "%" + value + "%");
            });

            fup.getEmail().ifPresent(value -> {
                filters.add("email like :email");
                params.put("email", "%" + value + "%");
            });

            fup.getPhoneNumber().ifPresent(value -> {
                filters.add("phoneNumber like :phoneNumber");
                params.put("phoneNumber", "%" + value + "%");
            });

            if (!filters.isEmpty()) baseQuery.append(" where ").append(String.join(" and ", filters));
//            fup.getSortParam().ifPresent(value -> baseQuery.append(" ").append(value.getQuery()));

            return session.createQuery(baseQuery.toString(), User.class)
                    .setProperties(params)
                    .list();
        }
    }
}
