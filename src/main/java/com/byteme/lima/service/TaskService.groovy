package com.byteme.lima.service

import com.byteme.lima.domain.Task
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class TaskService extends AbstractService {

    List<Task> findAll() {
        this.db.findAll(Task.class, "tasks")
    }

    Task findOne(String id) {
        this.db.getConverter().read(
                Task.class,
                this.db.getCollection("tasks").findOne(new ObjectId(id))
        )
    }

    Task findByCode(String code) {
        this.db.getConverter().read(
                Task.class,
                this.db.getCollection("tasks").findOne(
                        new BasicDBObject("code", code)
                )
        )
    }

    Task findByName(String name) {
        this.db.getConverter().read(
                Task.class,
                this.db.getCollection("tasks").findOne(
                        new BasicDBObject("name", name)
                )
        )
    }

    void remove(String id) {
        this.db.remove(new ObjectId(id))
    }

    void removeAll() {
        for (DBObject task : this.db.getCollection("tasks").find()) {
            this.db.remove(task, "tasks")
        }
    }

    void save(Task task) {
        this.db.save(task, "tasks")
    }

}
