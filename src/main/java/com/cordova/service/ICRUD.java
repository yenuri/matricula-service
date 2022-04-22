package com.cordova.service;

import com.cordova.pagination.PageSupport;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



public interface ICRUD<T, ID>{
    Mono<T> register(T t);

    Mono<T> modify(T t);

    Flux<T> findAll();

    Mono<T> findById(ID id);

    Mono<Void> remove(ID id);

    Mono<PageSupport<T>> listarPage(Pageable pageable);
}
