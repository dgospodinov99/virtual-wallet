package com.team01.web.virtualwallet.services.contracts;

import com.team01.web.virtualwallet.models.Role;

import java.util.List;

public interface RoleService extends BaseGetService<Role> {

    Role getByName(String name);
}
