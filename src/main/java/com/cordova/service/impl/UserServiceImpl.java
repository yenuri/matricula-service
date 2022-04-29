package com.cordova.service.impl;

import com.cordova.model.Usuario;
import com.cordova.repo.IGenericRepo;
import com.cordova.repo.IRolRepo;
import com.cordova.repo.IUserRepo;
import com.cordova.security.User;
import com.cordova.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends CRUDImpl<Usuario, String> implements IUserService {

    @Autowired
    private IUserRepo repo;

    @Autowired
    private IRolRepo rolRepo;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @Override
    protected IGenericRepo<Usuario, String> getRepo() {
        return repo;
    }

    @Override
    public Mono<User> buscarPorUsuario(String usuario) {
        Mono<Usuario> monoUsuario = repo.findOneByUsuario(usuario);

        List<String> roles = new ArrayList<String>();

        return monoUsuario.flatMap(u -> {
                    return Flux.fromIterable(u.getRoles())
                            .flatMap(rol -> {
                                return rolRepo.findById(rol.getId())
                                        .map(r -> {
                                            roles.add(r.getNombre());
                                            return r;
                                        });
                            }).collectList().flatMap(list -> {
                                u.setRoles(list);
                                return Mono.just(u);
                            });
                })
                .flatMap(u -> {
                    return Mono.just(new User(u.getUsuario(), u.getClave(), u.getEstado(), roles));
                });
    }

    @Override
    public Mono<Usuario> registrarHash(Usuario usuario) {
        usuario.setClave(bcrypt.encode(usuario.getClave()));
        return repo.save(usuario);
    }
}
