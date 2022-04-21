package com.cordova.service.impl;

import com.cordova.service.ICRUD;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
}
