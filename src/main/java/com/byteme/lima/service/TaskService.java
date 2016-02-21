package com.byteme.lima.service;

import com.byteme.lima.domain.Task;
import com.byteme.lima.domain.User;
import com.byteme.lima.exception.IllegalStateException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TaskService extends AbstractService {

    @Autowired
    public UserService userService;

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
                .map(it -> {
                    Task task = this.db.getConverter().read(Task.class, it);
                    task = this.fetchAssignee(task);
                    return task;
                })
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

    public List<Task> findAllByStatus(Task.Status status) {
        DBObject query = new BasicDBObject();
        query.put("status", status.name());

        return StreamSupport.stream(this.db.getCollection("tasks").find(query).spliterator(), true)
                .map(it -> this.db.getConverter().read(Task.class, it))
                .collect(Collectors.toList());
    }

    public Task fetchAssignee(Task task) {
        if (StringUtils.isNotBlank(task.assigneeId)) task.assignee = this.userService.findById(task.assigneeId);

        return task;
    }

    public List<Task> findAllByAssignee(String userId) {
        if (StringUtils.isNoneBlank(userId)) {
            DBObject query = new BasicDBObject();
            query.put("assigneeId", userId);

            return StreamSupport.stream(this.db.getCollection("tasks").find(query).spliterator(), true)
                    .map(it -> this.db.getConverter().read(Task.class, it))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public void remove(Task task) {
        this.db.remove(task, "tasks");
    }

    public Task save(Task task) {
        this.db.save(task, "tasks");
        return this.findByCode(task.getCode());
    }

    public void removeAll() {
        for (DBObject task : this.db.getCollection("tasks").find()) {
            this.db.remove(task, "tasks");
        }
    }

    public Task add(Task task, User user) throws IllegalStateException {
        if (user == null) throw new IllegalStateException("user is null");
        if (StringUtils.isBlank(user.id)) throw new IllegalStateException("user.id is null");
        if (task == null) throw new IllegalStateException("task is null");
        if (StringUtils.isBlank(task.id)) throw new IllegalStateException("task.id is null");

        task.assigneeId = user.id;

        task = this.fetchAssignee(task);
        return task;
    }

    public List<Task> fetchDue(Long dueDays) throws IllegalStateException {
        return this.fetchDueByUser(null, dueDays);
    }

    public List<Task> fetchDueByUser(User user, Long dueDays) throws IllegalStateException {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.add(Calendar.DATE, dueDays.intValue() * -1);

        DBObject query = new BasicDBObject();
        if (user != null)
            query.put("assigneeId", user.id);
        query.put("end", new BasicDBObject("$gte", start.getTime()).append("$lte", end.getTime()));

        return StreamSupport.stream(this.db.getCollection("tasks").find(query).spliterator(), false)
                .map(it -> this.db.getConverter().read(Task.class, it))
                .collect(Collectors.toList());
    }
}
