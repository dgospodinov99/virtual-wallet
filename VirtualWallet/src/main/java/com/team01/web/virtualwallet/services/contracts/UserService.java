package com.team01.web.virtualwallet.services.contracts;

import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.models.dto.FilterUserParams;

import java.util.List;

public interface UserService extends BaseGetService<User> {

    User getByUsername(String username);

    User getByEmail(String email);

    User getByWallet(Wallet wallet);

    void blockUserOnRegistration(User user);

    void unblockUserOnRegistration(User user);

    User blockUserByAdmin(String usernameToBlock, User executor);

    User unblockUserByAdmin(String usernameToBlock, User executor);

    List<User> filterUsers(FilterUserParams params);

    User search(String searchItem);

    void create(User user);

    void update(User user);

    void delete(int id);

    void updatePassword(User user, String newPassword);
}
