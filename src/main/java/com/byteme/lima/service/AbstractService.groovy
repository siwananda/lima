package com.byteme.lima.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate

abstract class AbstractService {
    @Autowired
    MongoTemplate db
}