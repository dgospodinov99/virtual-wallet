package com.team01.web.virtualwallet.repositories;



import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.repositories.contracts.BaseGetRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public abstract class BaseGetRepositoryImpl<T> implements BaseGetRepository<T> {

    private final Class<T> clazz;

    private final SessionFactory sessionFactory;

    @Autowired
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
    public <V> T getByField(String fieldName, V fieldValue) {
        try (Session session = sessionFactory.openSession()) {
            Query<T> query = session.createQuery(String.format("from %s where %s = :%s", clazz.getSimpleName(), fieldName, fieldName), clazz);
            query.setParameter(fieldName, fieldValue);

            List<T> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException(clazz.getSimpleName(), fieldName, fieldValue.toString());
            }

            return result.get(0);
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

