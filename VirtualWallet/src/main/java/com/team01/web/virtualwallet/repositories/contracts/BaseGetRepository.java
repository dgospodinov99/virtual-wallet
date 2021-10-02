package com.team01.web.virtualwallet.repositories.contracts;

import java.util.List;

public interface BaseGetRepository<T> {

    List<T> getAll();

    <V> T getByField(String fieldName, V fieldValue);

    T getById(int id);
}
