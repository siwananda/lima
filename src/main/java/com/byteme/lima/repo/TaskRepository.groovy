package com.byteme.lima.repo

import com.byteme.lima.domain.Task
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository extends MongoRepository<Task, String> {

}