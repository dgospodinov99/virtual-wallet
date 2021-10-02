package com.team01.web.virtualwallet.services.contracts;

import java.util.List;

public interface BaseGetService<T> {
    public abstract List<T> getAll();

    public abstract T getById(int id);
}
