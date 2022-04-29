package com.cordova.repo;

import com.cordova.model.Usuario;
import reactor.core.publisher.Mono;

public interface IUserRepo extends IGenericRepo<Usuario, String> {

    //SELECT * FROM USUARIO U WHERE U.USUARIO =?
    //{usuario: ?} --> en Mongo
    //DerivedQueries
    Mono<Usuario> findOneByUsuario(String usuario);
}
