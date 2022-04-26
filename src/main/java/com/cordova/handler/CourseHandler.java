package com.cordova.handler;

import com.cordova.model.Course;
import com.cordova.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class CourseHandler {

    @Autowired
    private ICourseService service;

    public Mono<ServerResponse> listar(ServerRequest req) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), Course.class);
    }

    public Mono<ServerResponse> listarPorId(ServerRequest req) {
        String id = req.pathVariable("id");

        return service.findById(id)
                .flatMap(c -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(c)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> registrar(ServerRequest req) {
        Mono<Course> monoCourse = req.bodyToMono(Course.class);

        return monoCourse
                .flatMap(service::register)
                .flatMap(c -> ServerResponse.created(URI.create(req.uri().toString().concat(c.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(c))
                );
    }

    public Mono<ServerResponse> modificar(ServerRequest req) {
        Mono<Course> monoCourse = req.bodyToMono(Course.class);
        Mono<Course> monoBD = service.findById(req.pathVariable("id"));
        return monoBD
                .zipWith(monoCourse, (bd, mc) -> {
                    bd.setId(mc.getId());
                    bd.setName(mc.getName());
                    bd.setAcronym(mc.getAcronym());
                    bd.setStatus(mc.getStatus());
                    return bd;
                })
                .flatMap(service::modify)
                .flatMap(c -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(c)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> eliminar(ServerRequest req) {
        String id = req.pathVariable("id");

        return service.findById(id)
                .flatMap(c -> service.remove(c.getId())
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
