package com.team01.web.virtualwallet.services.contracts;

import com.team01.web.virtualwallet.models.Role;

import java.util.List;

public interface RoleService {

    List<Role> getAll();

    Role getById(int id);

    Role getByName(String name);
}
