package com.team01.web.virtualwallet.repositories;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.Token;
import com.team01.web.virtualwallet.repositories.contracts.TokenRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TokenRepositoryImpl implements TokenRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TokenRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Token> getAllActive() {
        try (Session session = sessionFactory.openSession()) {
            Query<Token> query = session.createQuery("from Token where active = true order by id", Token.class);
            return query.list();
        }
    }

    @Override
    public Token getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Token> query = session.createQuery("from Token where id = :id", Token.class);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("Token", id);
            }
            return query.getSingleResult();
        }
    }

    @Override
    public List<Token> getUserTokens(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Token> query = session.createQuery("from Token where user.id = :id", Token.class);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("Tokens for user", "id", String.valueOf(id));
            }
            return query.list();
        }
    }

    @Override
    public Token getByToken(String token) {
        try (Session session = sessionFactory.openSession()) {
            Query<Token> query = session.createQuery("from Token where token = token", Token.class);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("Tokens", "code", token);
            }
            return query.getSingleResult();
        }
    }

    @Override
    public void create(Token token) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(token);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Token token) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(token);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            Token token = getById(id);
            token.setActive(false);
            session.beginTransaction();
            session.update(token);
            session.getTransaction().commit();
        }
    }
}
