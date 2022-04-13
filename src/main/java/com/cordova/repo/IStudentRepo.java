package com.cordova.repo;

import com.cordova.model.Student;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IStudentRepo extends ReactiveMongoRepository<Student, String> {
}
