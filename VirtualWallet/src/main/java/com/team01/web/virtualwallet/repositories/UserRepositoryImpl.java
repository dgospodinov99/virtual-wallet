package com.team01.web.virtualwallet.repositories;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.FilterUserParams;
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
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where active = true order by id", User.class);
            return query.list();
        }
    }

    @Override
    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
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
        try (Session session = sessionFactory.openSession()) {
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
        try (Session session = sessionFactory.openSession()) {
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
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where wallet.id = :id", User.class);
            query.setParameter("id", id);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("User", "Wallet-Id", String.valueOf(id));
            }
            return query.list().get(0);
        }
    }

//    @Override
//    public List<User> filterUsers(FilterUserParams params) {
//        try (Session session = sessionFactory.openSession()) {
//            String queryString = "from User where active = true ";
//
//            if (params.getEmail().isPresent()) {
//                queryString += "and email like concat('%', :email, '%') ";
//            }
//            if (params.getUsername().isPresent()) {
//                queryString += "and username like concat('%', :username, '%') ";
//            }
//            if (params.getPhoneNumber().isPresent()) {
//                queryString += "and phoneNumber like concat('%', :phoneNumber, '%') ";
//            }
//            if (params.getSortParam().isPresent()) {
//                queryString += params.getSortParam().get().getQuery();
//            }
//
//            Query<User> query = session.createQuery(queryString, User.class);
//
//            if (params.getEmail().isPresent()) {
//                query.setParameter("email", params.getEmail().get());
//            }
//            if (params.getUsername().isPresent()) {
//                query.setParameter("username", params.getUsername().get());
//            }
//            if (params.getPhoneNumber().isPresent()) {
//                query.setParameter("phoneNumber", params.getPhoneNumber().get());
//            }
//            if (params.getSortParam().isPresent()) {
//                query.setParameter("")
//            }
//            return query.list();
//        }
//    }

    @Override
    public List<User> filterUsers(FilterUserParams fup) {
        try (Session session = sessionFactory.openSession()) {
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

            fup.getSortParam().ifPresent(value -> baseQuery.append(" ").append(value.getQuery()));

            return session.createQuery(baseQuery.toString(), User.class)
                    .setProperties(params)
                    .list();
        }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = getById(id);
            session.delete(user);
            session.getTransaction().commit();
        }
    }


}
