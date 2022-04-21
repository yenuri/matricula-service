package com.cordova.service;

import com.cordova.model.Course;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICRUD<T, ID>{
    Mono<T> register(T t);

    Mono<T> modify(T t);

    Flux<T> findAll();

    Mono<T> findById(ID id);

    Mono<Void> remove(ID id);
}
