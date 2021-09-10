package com.team01.web.virtualwallet.services.contracts;

import com.team01.web.virtualwallet.models.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    void create(User user);

    void update(User user);

    void delete(int id);
}
