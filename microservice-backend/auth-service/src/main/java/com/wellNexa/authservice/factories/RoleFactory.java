package com.wellNexa.authservice.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wellNexa.authservice.enums.ERole;
import com.wellNexa.authservice.exceptions.RoleNotFoundException;
import com.wellNexa.authservice.modals.Role;
import com.wellNexa.authservice.services.RoleService;

@Component
public class RoleFactory {
    @Autowired
    RoleService roleService;

    public Role getInstance(String role) throws RoleNotFoundException {
        if (role.equals("admin")) {
            return roleService.findByName(ERole.ROLE_ADMIN);
        }
        else if (role.equals("user")){
            return roleService.findByName(ERole.ROLE_USER);
        }
        throw new RoleNotFoundException("Invalid role name: " + role);
    }

}
