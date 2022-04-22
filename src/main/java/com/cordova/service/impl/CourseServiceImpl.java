package com.cordova.service.impl;

import com.cordova.model.Course;
import com.cordova.pagination.PageSupport;
import com.cordova.repo.ICourseRepo;
import com.cordova.repo.IGenericRepo;
import com.cordova.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.awt.print.Pageable;

@Service
public class CourseServiceImpl extends CRUDImpl<Course, String> implements ICourseService {

    @Autowired
    private ICourseRepo repo;

    @Override
    protected IGenericRepo<Course, String> getRepo() {
        return repo;
    }

}
