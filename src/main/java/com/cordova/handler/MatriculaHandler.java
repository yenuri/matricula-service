package com.cordova.handler;

import com.cordova.model.Matricula;
import com.cordova.service.IMatriculaService;
import com.cordova.validators.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class MatriculaHandler {

    @Autowired
    private IMatriculaService service;

    @Autowired
    private RequestValidator validatorGeneral;

    public Mono<ServerResponse> listar(ServerRequest req) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), Matricula.class);
    }

    public Mono<ServerResponse> listarPorId(ServerRequest req) {
        String id = req.pathVariable("id");

        return service.findById(id)
                .flatMap(m -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(m)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> registrar(ServerRequest req) {
        Mono<Matricula> monoMatricula = req.bodyToMono(Matricula.class);

        return monoMatricula
                .flatMap(validatorGeneral::validate)
                .flatMap(service::register)
                .flatMap(s -> ServerResponse.created(URI.create(req.uri().toString().concat(s.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(s))
                );
    }

    public Mono<ServerResponse> modificar(ServerRequest req) {
        Mono<Matricula> monoMatricula = req.bodyToMono(Matricula.class);
        Mono<Matricula> monoBD = service.findById(req.pathVariable("id"));
        return monoBD
                .zipWith(monoMatricula, (bd, mm) -> {
                    bd.setId(mm.getId());
                    bd.setStudent(mm.getStudent());
                    bd.setCourseList(mm.getCourseList());
                    bd.setMatriculaDate(mm.getMatriculaDate());
                    return bd;
                })
                .flatMap(validatorGeneral::validate)
                .flatMap(service::modify)
                .flatMap(s -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(s)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> eliminar(ServerRequest req) {
        String id = req.pathVariable("id");

        return service.findById(id)
                .flatMap(s -> service.remove(s.getId())
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
