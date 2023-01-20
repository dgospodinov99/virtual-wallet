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
public class CardRepositoryImpl extends BaseModifyRepositoryImpl<Card> implements CardRepository {

    @Autowired
    public CardRepositoryImpl(SessionFactory sessionFactory) {
        super(Card.class, sessionFactory);
    }

    @Override
    public List<Card> getAll() {
        try (Session session = getSessionFactory().openSession()) {
            Query<Card> query = session.createQuery("from Card where active = true", Card.class);
            List<Card> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Card");
            }
            return result;
        }
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
    public List<Card> getUserCards(int id) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Card> query = session.createQuery("from Card where user.id = :id", Card.class);
            query.setParameter("id", id);
            return query.list();
        }
    }

}
