package com.team01.web.virtualwallet.repositories;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.Token;
import com.team01.web.virtualwallet.repositories.contracts.BaseModifyRepository;
import com.team01.web.virtualwallet.repositories.contracts.TokenRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TokenRepositoryImpl extends BaseModifyRepositoryImpl<Token> implements TokenRepository {

    @Autowired
    public TokenRepositoryImpl(SessionFactory sessionFactory) {
        super(Token.class, sessionFactory);
    }

    @Override
    public List<Token> getAllActive() {
        try (Session session = getSessionFactory().openSession()) {
            Query<Token> query = session.createQuery("from Token where active = true order by id", Token.class);
            return query.list();
        }
    }

    @Override
    public List<Token> getUserTokens(int id) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Token> query = session.createQuery("from Token where user.id = :id and active = true", Token.class);
            query.setParameter("id", id);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("Tokens for user", "id", String.valueOf(id));
            }
            return query.list();
        }
    }

    @Override
    public Token getByToken(String token) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Token> query = session.createQuery("from Token where token = :token", Token.class);
            query.setParameter("token", token);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("Tokens", "code", token);
            }
            return query.getSingleResult();
        }
    }
}
