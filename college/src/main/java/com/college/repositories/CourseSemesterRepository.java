package com.college.repositories;

import com.college.entities.CourseSemester;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseSemesterRepository extends MongoRepository<CourseSemester, String> {
}
