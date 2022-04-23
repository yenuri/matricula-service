package com.cordova.service.impl;

import com.cordova.model.Rol;
import com.cordova.repo.IGenericRepo;
import com.cordova.repo.IRolRepo;
import com.cordova.service.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolServiceImpl extends CRUDImpl<Rol, String> implements IRolService {

    @Autowired
    private IRolRepo repo;

    @Override
    protected IGenericRepo<Rol, String> getRepo() {
        return repo;
    }

}
