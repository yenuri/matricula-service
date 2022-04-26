package com.cordova.handler;

import com.cordova.model.Student;
import com.cordova.service.IStudentService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class StudentHandler {

    private IStudentService service;

    public Mono<ServerResponse> listar(ServerRequest req) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), Student.class);
    }

    public Mono<ServerResponse> listarPorId(ServerRequest req) {
        String id = req.pathVariable("id");

        return service.findById(id)
                .flatMap(p -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(p)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> registrar(ServerRequest req) {
        Mono<Student> monoStudent = req.bodyToMono(Student.class);

        return monoStudent
                .flatMap(service::register)
                .flatMap(s -> ServerResponse.created(URI.create(req.uri().toString().concat(s.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(s))
                );
    }

    public Mono<ServerResponse> modificar(ServerRequest req) {
        Mono<Student> monoStudent = req.bodyToMono(Student.class);
        Mono<Student> monoBD = service.findById(req.pathVariable("id"));
        return monoBD
                .zipWith(monoStudent, (bd, ms) -> {
                    bd.setId(ms.getId());
                    bd.setNames(ms.getNames());
                    bd.setLastNames(ms.getLastNames());
                    bd.setDni(ms.getDni());
                    bd.setAge(ms.getAge());
                    return bd;
                })
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
