package com.wellNexa.authservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wellNexa.authservice.enums.ERole;
import com.wellNexa.authservice.modals.Role;
import com.wellNexa.authservice.repositories.RoleRepository;

@Component
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByName(ERole eRole) {
        return roleRepository.findByName(eRole)
                .orElseThrow(() -> new RuntimeException("Role is not found."));
    }
}
