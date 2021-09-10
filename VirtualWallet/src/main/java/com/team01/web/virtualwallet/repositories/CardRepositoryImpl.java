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
public class CardRepositoryImpl implements CardRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CardRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Card> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Card> query = session.createQuery("from Card order by id", Card.class);
            return query.list();
        }
    }

    @Override
    public Card getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Card card = session.get(Card.class, id);
            if (card == null){
                throw new EntityNotFoundException("Card",id);
            }
            return card;
        }
    }

    @Override
    public Card getByCardNumber(String cardNumber){
        try (Session session = sessionFactory.openSession()) {
            Query<Card> query = session.createQuery("from Card where cardNumber = :cardNumber", Card.class);
            query.setParameter("cardNumber",cardNumber);
            List<Card> result = query.list();
            if(result.size()==0){
                throw new EntityNotFoundException("Card","card number",cardNumber);
            }
            return result.get(0);
        }
    }

    @Override
    public void create(Card card) {
        try (Session session = sessionFactory.openSession()) {
            session.save(card);
        }
    }

    @Override
    public void update(Card card) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(card);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Card card = getById(id);
            session.delete(card);
            session.getTransaction().commit();
        }
    }


}
