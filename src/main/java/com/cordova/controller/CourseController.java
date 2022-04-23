package com.cordova.controller;

import com.cordova.model.Course;
import com.cordova.pagination.PageSupport;
import com.cordova.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;
import static reactor.function.TupleUtils.function;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private ICourseService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<Course>>> findAll() {
        Flux<Course> fxCourse = service.findAll();
        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fxCourse));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Course>> findById(@PathVariable("id") String id) {
        return service.findById(id)
                .map(p -> ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p));
    }

    @PostMapping
    public Mono<ResponseEntity<Course>> register(@Valid @RequestBody Course course, final ServerHttpRequest request) {
        return service.register(course)
                .map(p -> ResponseEntity
                        .created(URI.create(request.getURI().toString().concat("/").concat(p.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Course>> modify(@Valid @RequestBody Course course, @PathVariable("id") String id) {
        Mono<Course> monoCourseBody = Mono.just(course);
        Mono<Course> monoCourseBd = service.findById(id);
        return monoCourseBd
                .zipWith(monoCourseBody, (bd, cr) -> {
                    bd.setId(id);
                    bd.setAcronym(cr.getAcronym());
                    bd.setName(cr.getName());
                    bd.setStatus(cr.getStatus());
                    return bd;
                })
                .flatMap(service::modify)
                .map(cr -> ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(cr))
                .defaultIfEmpty(new ResponseEntity<Course>(HttpStatus.NOT_FOUND));
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
    public Mono<EntityModel<Course>> findByIdHateoas(@PathVariable("id") String id) {
        Mono<Link> link = linkTo(methodOn(CourseController.class).findById(id)).withSelfRel().toMono();
        Mono<Link> link2 = linkTo(methodOn(CourseController.class).findById(id)).withSelfRel().toMono();

        return link
                .zipWith(link2)
                .map(function((lk, lk2) -> Links.of(lk, lk2)))
                .zipWith(service.findById(id), (lks, s) -> EntityModel.of(s, lks));
    }

    @GetMapping("/pageable")
    public Mono<ResponseEntity<PageSupport<Course>>> listarPageable(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        Pageable pageRequest = PageRequest.of(page, size);

        return service.listarPage(pageRequest)
                .map(pag -> ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(pag))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
