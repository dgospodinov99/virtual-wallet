package com.team01.web.virtualwallet.repositories;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.repositories.contracts.CardRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CardRepositoryImpl extends BaseGetRepositoryImpl<Card> implements CardRepository {

    @Autowired
    public CardRepositoryImpl(SessionFactory sessionFactory) {
        super(Card.class, sessionFactory);
    }

    @Override
    public Card getByCardNumber(String cardNumber) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Card> query = session.createQuery("from Card where cardNumber = :cardNumber", Card.class);
            query.setParameter("cardNumber", cardNumber);
            List<Card> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Card", "card number", cardNumber);
            }
            return result.get(0);
        }
    }

    @Override
    public void create(Card card) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(card);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Card card) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(card);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            Card card = getById(id);
            session.delete(card);
            session.getTransaction().commit();
        }
    }


}
