package com.byteme.lima.service;

import com.byteme.lima.domain.Task;
import com.byteme.lima.domain.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public List<Task> findAllByIds(List<String> ids) {
        List<ObjectId> memberObjectId = ids.parallelStream()
                .map(ObjectId::new)
                .collect(Collectors.toList());
        DBObject query = QueryBuilder.start("_id").in(memberObjectId).get();

        return StreamSupport.stream(this.db.getCollection("tasks").find(query).spliterator(), true)
                .map(it -> this.db.getConverter().read(Task.class, it))
                .collect(Collectors.toList());

    }

    public Task findByCode(String code) {
        return this.db.getConverter().read(
                Task.class,
                this.db.getCollection("tasks").findOne(
                        new BasicDBObject("code", code)
                )
        );
    }

    public List<Task> findByStatus(Task.Status status) {
        DBObject query = new BasicDBObject();
        query.put("status", status.name());

        return StreamSupport.stream(this.db.getCollection("tasks").find(query).spliterator(), false)
                .map(it -> this.db.getConverter().read(Task.class, it))
                .collect(Collectors.toList());
    }

    public void remove(Task task) {
        this.db.remove(task, "tasks");
    }

    public Task save(Task task) {
        this.db.save(task, "tasks");
        return this.findByCode(task.getCode());
    }

    public void removeAll() {
        for (DBObject task: this.db.getCollection("tasks").find()) {
            this.db.remove(task, "tasks");
        }
    }

    public void bootstrap() throws IOException {
        this.removeAll();

        List<Task> tasks = new ObjectMapper().readValue(this.resourceLoader.getResource("classpath:tasks.json").getFile(), new TypeReference<List<Task>>() { });
        for (Task task: tasks) {
            this.save(task);
        }
    }
}
