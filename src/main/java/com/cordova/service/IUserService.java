package com.cordova.service;

import com.cordova.model.Usuario;
import com.cordova.security.User;
import reactor.core.publisher.Mono;

public interface IUserService extends ICRUD<Usuario, String> {

    Mono<Usuario> registrarHash(Usuario usuario);

    Mono<User> buscarPorUsuario(String usuario);
}
