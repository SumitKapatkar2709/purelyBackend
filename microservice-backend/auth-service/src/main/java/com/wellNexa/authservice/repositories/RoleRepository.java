package com.wellNexa.authservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.wellNexa.authservice.enums.ERole;
import com.wellNexa.authservice.modals.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
    boolean existsByName(ERole name);
}