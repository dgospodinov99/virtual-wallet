package com.team01.web.virtualwallet.repositories;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.Role;
import com.team01.web.virtualwallet.repositories.contracts.RoleRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public RoleRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Role> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Role> query = session.createQuery("from Role order by id", Role.class);
            return query.list();
        }
    }

    @Override
    public Role getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Role role = session.get(Role.class, id);
            if (role == null) {
                throw new EntityNotFoundException("Role", id);
            }
            return role;
        }
    }

    @Override
    public Role getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Role> query = session.createQuery("from Role where name = :name", Role.class);
            query.setParameter("name", name);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("Role", "name", name);
            }
            return query.list().get(0);
        }
    }
}
