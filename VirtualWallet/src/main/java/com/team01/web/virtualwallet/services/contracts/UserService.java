package com.team01.web.virtualwallet.services.contracts;

import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.Wallet;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByWallet(Wallet wallet);

    User blockUser(String usernameToBlock, User executor);

    User unblockUser(String usernameToBlock, User executor);

    void create(User user);

    void update(User user);

    void delete(int id);
}
