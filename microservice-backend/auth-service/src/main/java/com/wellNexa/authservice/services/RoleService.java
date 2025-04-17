package com.wellNexa.authservice.services;

import org.springframework.stereotype.Service;

import com.wellNexa.authservice.enums.ERole;
import com.wellNexa.authservice.modals.Role;

@Service
public interface RoleService {
    Role findByName(ERole eRole);
}