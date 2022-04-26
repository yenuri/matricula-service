package com.cordova;

import com.cordova.handler.CourseHandler;
import com.cordova.handler.MatriculaHandler;
import com.cordova.handler.StudentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routesCourse(CourseHandler handler){
        return route(GET("/v2/courses"), handler::listar)
                .andRoute(GET("/v2/courses/{id}"), handler::listarPorId)
                .andRoute(POST("/v2/courses"), handler::registrar)
                .andRoute(PUT("/v2/courses/{id}"), handler::modificar)
                .andRoute(DELETE("/v2/courses/{id}"), handler::eliminar);
    }

    @Bean
    public RouterFunction<ServerResponse> routesStudent(StudentHandler handler){
        return route(GET("/v2/students"), handler::listar)
                .andRoute(GET("/v2/students/{id}"), handler::listarPorId)
                .andRoute(POST("/v2/students"), handler::registrar)
                .andRoute(PUT("/v2/students/{id}"), handler::modificar)
                .andRoute(DELETE("/v2/students/{id}"), handler::eliminar);
    }

    @Bean
    public RouterFunction<ServerResponse> routesMatricula(MatriculaHandler handler){
        return route(GET("/v2/matriculas"), handler::listar)
                .andRoute(GET("/v2/matriculas/{id}"), handler::listarPorId)
                .andRoute(POST("/v2/matriculas"), handler::registrar)
                .andRoute(PUT("/v2/matriculas/{id}"), handler::modificar)
                .andRoute(DELETE("/v2/matriculas/{id}"), handler::eliminar);
    }
}

