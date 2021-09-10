package com.team01.web.virtualwallet.repositories.contracts;

import com.team01.web.virtualwallet.models.Role;

import java.util.List;

public interface RoleRepository {

    List<Role> getAll();

    Role getById(int id);

    Role getByName(String name);
}
