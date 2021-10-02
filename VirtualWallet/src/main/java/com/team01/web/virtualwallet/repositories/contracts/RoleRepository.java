package com.team01.web.virtualwallet.repositories.contracts;

import com.team01.web.virtualwallet.models.Role;

public interface RoleRepository extends BaseGetRepository<Role>{

    Role getByName(String name);
}
