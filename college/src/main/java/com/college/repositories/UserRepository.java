package com.college.repositories;

import com.college.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository  extends MongoRepository<User, String> {
    Optional<User> findUserByUsername(String username);
}
