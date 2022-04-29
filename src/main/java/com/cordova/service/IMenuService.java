package com.cordova.service;

import com.cordova.model.Menu;
import reactor.core.publisher.Flux;

public interface IMenuService extends ICRUD<Menu, String>{

    Flux<Menu> obtenerMenus(String[] roles);
}
