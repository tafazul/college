package com.college.repositories;

import com.college.entities.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TeacherRepository extends MongoRepository<Teacher, String> {
    Optional<Teacher> findBySecurityUserId(String ssecurityUserId);
}
