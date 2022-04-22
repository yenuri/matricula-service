package com.cordova.service.impl;

import com.cordova.pagination.PageSupport;
import com.cordova.service.ICRUD;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

public abstract class CRUDImpl<T, ID> implements ICRUD<T, ID> {

    protected abstract ReactiveMongoRepository<T, ID> getRepo();

    @Override
    public Mono<T> register(T t) {
        return getRepo().save(t);
    }

    @Override
    public Mono<T> modify(T t) {
        return getRepo().save(t);
    }

    @Override
    public Flux<T> findAll() {
        return getRepo().findAll();
    }

    @Override
    public Mono<T> findById(ID id) {
        return getRepo().findById(id);
    }

    @Override
    public Mono<Void> remove(ID id) {
        return getRepo().deleteById(id);
    }

    public Mono<PageSupport<T>> listarPage(Pageable page) {
        return getRepo().findAll() //Flux<T>
                .collectList() //Mono<List<T>>
                .map(list -> new PageSupport<>(
                        list.stream()
                                .skip(page.getPageNumber() * page.getPageSize())
                                .limit(page.getPageSize()).collect(Collectors.toList()),
                        page.getPageNumber(), page.getPageSize(), list.size()));
    }
}
