package com.team01.web.virtualwallet.repositories.contracts;

import java.util.List;

public interface BaseGetRepository<T> {

    List<T> getAll();

    T getById(int id);
}
