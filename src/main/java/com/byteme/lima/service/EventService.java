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

        return event;
    }

    public Event createEvent(String message) throws IllegalStateException {
        Integer id = this.bootstrapService.next();
        Event event = new Event();
        event.code = "Event-" + id;
        event.name = message;
        event.timestamp = new Date();

        this.db.save(event, "events");
        event = this.findByCode(event.getCode());

        return event;
    }
}
