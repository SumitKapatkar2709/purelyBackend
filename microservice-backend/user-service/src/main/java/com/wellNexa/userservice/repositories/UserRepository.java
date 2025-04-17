package com.wellNexa.userservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.wellNexa.userservice.modals.User;



@Repository
public interface UserRepository extends MongoRepository<User, String> {

}
