package com.cordova.service;

import com.cordova.model.Student;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IStudentService {

    Mono<Student> register(Student student);

    Mono<Student> modify(Student student);

    Flux<Student> findAll();

    Mono<Student> findById(String id);

    Mono<Void> remove(String id);
}
