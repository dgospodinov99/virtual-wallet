package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.models.Role;
import com.team01.web.virtualwallet.repositories.contracts.RoleRepository;
import com.team01.web.virtualwallet.services.contracts.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl extends BaseGetServiceImpl<Role> implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getByName(String name) {
        return roleRepository.getByName(name);
    }
}
