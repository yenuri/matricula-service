package com.cordova.service.impl;

import com.cordova.model.Student;
import com.cordova.repo.IStudentRepo;
import com.cordova.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private IStudentRepo repo;

    @Override
    public Mono<Student> register(Student student) {
        return repo.save(student);
    }

    @Override
    public Mono<Student> modify(Student student) {
        return repo.save(student);
    }

    @Override
    public Flux<Student> findAll() {
        return repo.findAll();
    }

    @Override
    public Mono<Student> findById(String id) {
        return repo.findById(id);
    }

    @Override
    public Mono<Void> remove(String id) {
        return repo.deleteById(id);
    }
}
