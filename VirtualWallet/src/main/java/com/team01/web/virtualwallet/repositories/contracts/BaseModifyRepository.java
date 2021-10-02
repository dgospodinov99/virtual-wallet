package com.team01.web.virtualwallet.repositories.contracts;

public interface BaseModifyRepository<T> {

    void update(T entity);

    void create(T entity);

    void delete(int id);
}
