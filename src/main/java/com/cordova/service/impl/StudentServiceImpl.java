package com.cordova.service.impl;

import com.cordova.model.Student;
import com.cordova.repo.IGenericRepo;
import com.cordova.repo.IStudentRepo;
import com.cordova.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends CRUDImpl<Student, String> implements IStudentService {

    @Autowired
    private IStudentRepo repo;

    @Override
    protected IGenericRepo<Student, String> getRepo() {
        return repo;
    }
}
