package com.cordova.controller;

import com.cordova.model.Student;
import com.cordova.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;
import static reactor.function.TupleUtils.function;

import java.net.URI;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private IStudentService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<Student>>> findAll() {
        Flux<Student> fxStudent = service.findAll();
        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fxStudent));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Student>> findById(@PathVariable("id") String id) {
        return service.findById(id)
                .map(p -> ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p));
    }

    @PostMapping
    public Mono<ResponseEntity<Student>> register(@RequestBody Student student, final ServerHttpRequest request) {
        return service.register(student)
                .map(p -> ResponseEntity
                        .created(URI.create(request.getURI().toString().concat("/").concat(p.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Student>> modify(@PathVariable("id") String id, @RequestBody Student student) {
        Mono<Student> monoStudentBody = Mono.just(student);
        Mono<Student> monoStudentBd = service.findById(id);
        return monoStudentBd
                .zipWith(monoStudentBody, (bd, st) -> {
                    bd.setId(id);
                    bd.setNames(st.getNames());
                    bd.setLastNames(st.getLastNames());
                    bd.setDni(st.getDni());
                    bd.setAge(st.getAge());
                    return bd;
                })
                .flatMap(service::modify)
                .map(st -> ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(st))
                .defaultIfEmpty(new ResponseEntity<Student>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> remove(@PathVariable("id") String id) {

        return service.findById(id)
                .flatMap(p -> {
                    return service.remove(p.getId())
                            .thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
                })
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/hateoas/{id}")
    public Mono<EntityModel<Student>> findByIdHateoas(@PathVariable("id") String id) {
        Mono<Link> link = linkTo(methodOn(StudentController.class).findById(id)).withSelfRel().toMono();
        Mono<Link> link2 = linkTo(methodOn(StudentController.class).findById(id)).withSelfRel().toMono();

        // 1 link
        /*return service.findById(id)
                .zipWith(link, (s, lk) -> EntityModel.of(s, lk));*/
        // + de 1 link
        return link
                .zipWith(link2)
                .map(function((lk, lk2) -> Links.of(lk, lk2)))
                .zipWith(service.findById(id), (lks, s) -> EntityModel.of(s, lks));
    }
}
