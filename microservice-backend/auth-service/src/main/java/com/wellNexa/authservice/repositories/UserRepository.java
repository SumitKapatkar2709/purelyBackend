package com.wellNexa.authservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.wellNexa.authservice.modals.User;

import java.util.Optional;


@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    User findByVerificationCode(String verificationCode);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
