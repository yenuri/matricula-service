package com.cordova.service.impl;

import com.cordova.model.Menu;
import com.cordova.repo.IGenericRepo;
import com.cordova.repo.IMenuRepo;
import com.cordova.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MenuServiceImpl extends CRUDImpl<Menu, String> implements IMenuService {

    @Autowired
    private IMenuRepo repo;

    @Autowired
    protected IGenericRepo<Menu,String> getRepo(){
        return repo;
    }

    @Override
    public Flux<Menu> obtenerMenus(String[] roles) {
        return repo.obtenerMenus(roles);
    }
}
