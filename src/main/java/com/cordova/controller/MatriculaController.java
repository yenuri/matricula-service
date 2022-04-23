package com.cordova.controller;

import com.cordova.model.Matricula;
import com.cordova.pagination.PageSupport;
import com.cordova.service.IMatriculaService;
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
@RequestMapping("/matriculas")
public class MatriculaController {

    @Autowired
    private IMatriculaService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<Matricula>>> findAll() {
        Flux<Matricula> fxMatricula = service.findAll();
        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fxMatricula));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Matricula>> findById(@PathVariable("id") String id) {
        return service.findById(id)
                .map(p -> ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p));
    }

    @PostMapping
    public Mono<ResponseEntity<Matricula>> register(@Valid @RequestBody Matricula matricula, final ServerHttpRequest request) {
        return service.register(matricula)
                .map(p -> ResponseEntity
                        .created(URI.create(request.getURI().toString().concat("/").concat(p.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Matricula>> modify(@Valid @RequestBody Matricula matricula, @PathVariable("id") String id) {
        Mono<Matricula> monoMatriculaBody = Mono.just(matricula);
        Mono<Matricula> monoMatriculaBd = service.findById(id);
        return monoMatriculaBd
                .zipWith(monoMatriculaBody, (bd, mt) -> {
                    bd.setId(id);
                    bd.setStudent(mt.getStudent());
                    bd.setCourseList(mt.getCourseList());
                    bd.setMatriculaDate(mt.getMatriculaDate());
                    return bd;
                })
                .flatMap(service::modify)
                .map(mt -> ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mt))
                .defaultIfEmpty(new ResponseEntity<Matricula>(HttpStatus.NOT_FOUND));
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
    public Mono<EntityModel<Matricula>> findByIdHateoas(@PathVariable("id") String id) {
        Mono<Link> link = linkTo(methodOn(MatriculaController.class).findById(id)).withSelfRel().toMono();
        Mono<Link> link2 = linkTo(methodOn(MatriculaController.class).findById(id)).withSelfRel().toMono();

        return link
                .zipWith(link2)
                .map(function((lk, lk2) -> Links.of(lk, lk2)))
                .zipWith(service.findById(id), (lks, s) -> EntityModel.of(s, lks));
    }

    @GetMapping("/pageable")
    public Mono<ResponseEntity<PageSupport<Matricula>>> listarPageable(
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
