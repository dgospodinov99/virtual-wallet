package com.team01.web.virtualwallet.repositories;

import com.team01.web.virtualwallet.repositories.contracts.BaseModifyRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public abstract class BaseModifyRepositoryImpl<T> extends BaseGetRepositoryImpl<T> implements BaseModifyRepository<T> {

    public BaseModifyRepositoryImpl(Class<T> clazz, SessionFactory sessionFactory) {
        super(clazz, sessionFactory);
    }

    @Override
    public void update(T entity) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void create(T entity) {
        try (Session session = getSessionFactory().openSession()) {
            session.save(entity);
        }
    }

    @Override
    public void delete(int id) {
        T toDelete = getById(id);
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(toDelete);
            session.getTransaction().commit();
        }
    }
}
