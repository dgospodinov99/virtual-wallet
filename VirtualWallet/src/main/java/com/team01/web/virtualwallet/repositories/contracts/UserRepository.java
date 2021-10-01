package com.team01.web.virtualwallet.repositories.contracts;

import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.FilterUserParams;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByWallet(int id);

    User getByPhoneNumber(String phoneNumber);

    User search(String searchItem);

    void create(User user);

    void update(User user);

    void delete(int id);

    List<User> filterUsers(FilterUserParams params);

}
