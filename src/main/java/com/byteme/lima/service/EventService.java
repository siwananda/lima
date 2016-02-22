package com.byteme.lima.service;

import com.byteme.lima.domain.*;
import com.byteme.lima.exception.IllegalStateException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EventService extends AbstractService {

    @Autowired
    public BootstrapService bootstrapService;

    @Autowired
    public ProjectService projectService;

    @Autowired
    public TaskService taskService;

    @Autowired
    public TeamService teamService;

    @Autowired
    public UserService userService;

    public List<Event> findAllByIds(List<String> ids) {
        if (ids != null && !CollectionUtils.isEmpty(ids)) {
            List<ObjectId> objectIds = ids.parallelStream()
                    .map(ObjectId::new)
                    .collect(Collectors.toList());

            DBObject query = QueryBuilder.start("_id").in(objectIds).get();

            return StreamSupport.stream(this.db.getCollection("events").find(query).spliterator(), true)
                    .map(it -> this.db.getConverter().read(Event.class, it))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<Event> findByProject(Project project) {
        if (project != null && project.historyIds != null && !CollectionUtils.isEmpty(project.historyIds)) {
            project.history = new ArrayList<>();
            project.history.addAll(this.findAllByIds(project.historyIds));
            return project.history;
        }

        return null;
    }

    public List<Event> findByTask(Task task) {
        if (task != null && task.historyIds != null && !CollectionUtils.isEmpty(task.historyIds)) {
            task.history = new ArrayList<>();
            task.history.addAll(this.findAllByIds(task.historyIds));
            return task.history;
        }

        return null;
    }

    public List<Event> findByTeam(Team team) {
        if (team != null && team.historyIds != null && !CollectionUtils.isEmpty(team.historyIds)) {
            team.history = new ArrayList<>();
            team.history.addAll(this.findAllByIds(team.historyIds));
            return team.history;
        }

        return null;
    }

    public List<Event> findByUser(User user) {
        if (user != null && user.historyIds != null && !CollectionUtils.isEmpty(user.historyIds)) {
            user.history = new ArrayList<>();
            user.history.addAll(this.findAllByIds(user.historyIds));
            return user.history;
        }

        return null;
    }

    public Event findByCode(String code) {
        return this.db.getConverter().read(
                Event.class,
                this.db.getCollection("events").findOne(
                        new BasicDBObject("code", code)
                )
        );
    }

    public Event assign(Task task, User user) throws IllegalStateException {
        return this.createEvent(task, " assigned to ", user);
    }

    public Event assign(Project project, Team team) throws IllegalStateException {
        return this.createEvent(project, " assigned to ", team);
    }

    public Event add(Project project, Task task) throws IllegalStateException {
        return this.createEvent(project, " creates additional task: ", task);
    }

    public Event assign(User user, Team team) throws IllegalStateException {
        return this.createEvent(user, " assigned to ", team);
    }

    public Event createEvent(BaseDomain source, String message, BaseDomain target) throws IllegalStateException {
        if (source == null || source.id == null || StringUtils.isBlank(source.id)) throw new IllegalStateException("source is empty");
        if (target == null || target.id == null || StringUtils.isBlank(target.id)) throw new IllegalStateException("target is empty");

        Integer id = this.bootstrapService.next();
        Event event = new Event();
        event.code = "Event-" + id;
        event.name = source.name + message + target.name;
        event.timestamp = new Date();

        this.db.save(event, "events");
        event = this.findByCode(event.getCode());

        this.saveSource(source, event);

        return event;
    }

    public Event createEvent(BaseDomain source, String message) throws IllegalStateException {
        if (source == null || source.id == null || StringUtils.isBlank(source.id)) throw new IllegalStateException("source is empty");

        Integer id = this.bootstrapService.next();
        Event event = new Event();
        event.code = "Event-" + id;
        event.name = source.name + message;
        event.timestamp = new Date();

        this.db.save(event, "events");
        event = this.findByCode(event.getCode());

        this.saveSource(source, event);

        return event;
    }

    public void saveSource(BaseDomain source, Event event) {
        source.addEvent(event);

        if (source instanceof Project)  this.projectService.save((Project) source);
        if (source instanceof Task)     this.taskService.save((Task) source);
        if (source instanceof Team)     this.teamService.save((Team) source);
        if (source instanceof User)     this.userService.save((User) source);
    }

    public void removeAll() {
        for (DBObject project: this.db.getCollection("events").find()) {
            this.db.remove(project, "events");
        }
    }

}
