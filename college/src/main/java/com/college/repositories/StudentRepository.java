package com.college.repositories;

import com.college.entities.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findBySecurityUserId(String securityUserId);
}
