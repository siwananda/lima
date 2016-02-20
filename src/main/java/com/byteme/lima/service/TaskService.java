package com.byteme.lima.service;

import com.byteme.lima.domain.Task;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService extends AbstractService {

    public List<Task> findAll() {
        return this.db.findAll(Task.class, "tasks");
    }

    public Task findById(String id) {
        return this.db.getConverter().read(
                Task.class,
                this.db.getCollection("tasks").findOne(new ObjectId(id))
        );
    }

    public Task findByCode(String code) {
        return this.db.getConverter().read(
                Task.class,
                this.db.getCollection("tasks").findOne(
                        new BasicDBObject("code", code)
                )
        );
    }

    public void remove(Task task) {
        this.db.remove(task, "tasks");
    }

    public void save(Task task) {
        this.db.save(task, "tasks");
    }
}
