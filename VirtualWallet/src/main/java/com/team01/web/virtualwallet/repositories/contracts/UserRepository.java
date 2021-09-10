package com.team01.web.virtualwallet.repositories.contracts;

import com.team01.web.virtualwallet.models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    void create(User user);

    void update(User user);

    void delete(int id);
}
