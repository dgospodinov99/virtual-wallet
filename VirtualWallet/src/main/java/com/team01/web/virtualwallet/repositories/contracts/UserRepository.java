package com.team01.web.virtualwallet.repositories.contracts;

import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.FilterUserParams;

import java.util.List;

public interface UserRepository extends BaseGetRepository<User>, BaseModifyRepository<User> {

    User getByUsername(String username);    //field

    User getByEmail(String email);  //field

    User getByWallet(int id);

    User getByPhoneNumber(String phoneNumber); //field

    User search(String searchItem);

    List<User> filterUsers(FilterUserParams params);

}
