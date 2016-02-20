package com.byteme.lima.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class AbstractService {
    @Autowired
    public Environment env;

    @Autowired
    public MongoTemplate db;
}