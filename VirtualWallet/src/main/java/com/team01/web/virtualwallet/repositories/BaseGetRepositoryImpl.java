package com.team01.web.virtualwallet.repositories;


import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.repositories.contracts.BaseGetRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;


public abstract class BaseGetRepositoryImpl<T> implements BaseGetRepository<T> {

    private final Class<T> clazz;

    private final SessionFactory sessionFactory;

    public BaseGetRepositoryImpl(Class<T> clazz, SessionFactory sessionFactory) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public List<T> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<T> query = session.createQuery("from " + clazz.getSimpleName(), clazz);
            return query.list();
        }
    }

    @Override
    public T getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            T object = session.get(clazz, id);
            if (object == null) {
                throw new EntityNotFoundException(clazz.getSimpleName(), id);
            }
            return object;
        }
    }
}

