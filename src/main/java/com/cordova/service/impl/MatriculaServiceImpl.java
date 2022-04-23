package com.cordova.service.impl;

import com.cordova.model.Matricula;
import com.cordova.repo.IGenericRepo;
import com.cordova.repo.IMatriculaRepo;
import com.cordova.service.IMatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatriculaServiceImpl extends CRUDImpl<Matricula, String> implements IMatriculaService {

    @Autowired
    private IMatriculaRepo repo;

    @Override
    protected IGenericRepo<Matricula, String> getRepo() {
        return repo;
    }

}
