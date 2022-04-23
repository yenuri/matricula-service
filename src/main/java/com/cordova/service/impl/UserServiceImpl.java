package com.cordova.service.impl;

import com.cordova.model.User;
import com.cordova.repo.IGenericRepo;
import com.cordova.repo.IUserRepo;
import com.cordova.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends CRUDImpl<User, String> implements IUserService {

    @Autowired
    private IUserRepo repo;

    @Override
    protected IGenericRepo<User, String> getRepo() {
        return repo;
    }

}
