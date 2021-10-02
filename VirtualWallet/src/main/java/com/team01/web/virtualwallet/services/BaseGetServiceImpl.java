package com.team01.web.virtualwallet.services;



import com.team01.web.virtualwallet.repositories.contracts.BaseGetRepository;
import com.team01.web.virtualwallet.services.contracts.BaseGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class BaseGetServiceImpl<T> implements BaseGetService<T> {

    private final BaseGetRepository<T> repository;

    @Autowired
    public BaseGetServiceImpl(BaseGetRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public List<T> getAll() {
        return repository.getAll();
    }

    @Override
    public T getById(int id) {
        return repository.getById(id);
    }
}
