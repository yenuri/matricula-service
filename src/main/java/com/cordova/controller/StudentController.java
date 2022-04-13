package com.cordova.controller;

import com.cordova.model.Student;
import com.cordova.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private IStudentService service;

    @GetMapping
    public Flux<Student> findAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Student> findById(@PathVariable("id") String id){
        return service.findById(id);
    }

    @PostMapping
    public Mono<Student> register(@RequestBody Student student){
        return service.register(student);
    }

    @PutMapping
    public Mono<Student> modify(@RequestBody Student student){
        return service.modify(student);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> remove(@PathVariable("id") String id){
        return service.remove(id);
    }
}
