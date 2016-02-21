package com.byteme.lima.service;

import com.byteme.lima.domain.*;
import com.byteme.lima.exception.IllegalStateException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProjectService extends AbstractService {

    @Autowired
    public EventService eventService;

    @Autowired
    public TeamService teamService;

    @Autowired
    public TaskService taskService;

    public List<Project> findAll() {
        return this.db.findAll(Project.class, "projects");
    }

    public Project findById(String id) {
        return this.db.getConverter().read(
                Project.class,
                this.db.getCollection("projects").findOne(new ObjectId(id))
        );
    }

    public Project findByCode(String code) {
        return this.db.getConverter().read(
                Project.class,
                this.db.getCollection("projects").findOne(
                        new BasicDBObject("code", code)
                )
        );
    }

    public List<Project> findAllByName(String name) {
        DBObject query = new BasicDBObject();
        query.put("name", name);

        return StreamSupport.stream(this.db.getCollection("projects").find(query).spliterator(), false)
                .map(it -> this.db.getConverter().read(Project.class, it))
                .collect(Collectors.toList());
    }

    public List<Project> findAllByStatus(Project.Status status) {
        DBObject query = new BasicDBObject();
        query.put("status", status.name());

        return StreamSupport.stream(this.db.getCollection("projects").find(query).spliterator(), false)
                .map(it -> this.db.getConverter().read(Project.class, it))
                .collect(Collectors.toList());
    }

    public Project fetchTeam(Project project) {
        if (project.teamId != null) {
            project.team = this.teamService.findById(project.teamId);
        }
        return project;
    }

    public Project fetchTasks(Project project) {
        if (project.taskIds != null) {
            project.tasks = new ArrayList<>();
            project.tasks.addAll(this.taskService.findAllByIds(project.taskIds));
        }
        return project;
    }

    public Project fetchHistory(Project project) {
        if (project.historyIds != null) {
            project.history = new ArrayList<>();
            project.history.addAll(this.eventService.findAllByIds(project.historyIds));
        }
        return project;
    }

    public void remove(Project project) {
        this.db.remove(project, "projects");
    }

    public Project save(Project project) {
        this.db.save(project, "projects");
        return this.findByCode(project.getCode());
    }

    public void removeAll() {
        for (DBObject project: this.db.getCollection("projects").find()) {
            this.db.remove(project, "projects");
        }
    }

    public Project add(Project project, Task task) throws IllegalStateException {
        if (project == null) throw new IllegalStateException("project is null");
        if (StringUtils.isBlank(project.id)) throw new IllegalStateException("project.id is null");
        if (task == null) throw new IllegalStateException("task is null");
        if (StringUtils.isBlank(task.id)) throw new IllegalStateException("task.id is null");

        if (project.taskIds == null) project.taskIds = new ArrayList<>();

        project.taskIds.add(task.id);

        project = this.fetchTasks(project);
        return project;
    }

    public Project remove(Project project, Task task) throws IllegalStateException {
        if (project == null) throw new IllegalStateException("project is null");
        if (StringUtils.isBlank(project.id)) throw new IllegalStateException("project.id is null");
        if (task == null) throw new IllegalStateException("task is null");
        if (StringUtils.isBlank(task.id)) throw new IllegalStateException("task.id is null");
        if (project.taskIds == null) throw new IllegalStateException("task is null");
        if (project.taskIds.isEmpty()) throw new IllegalStateException("taskIds is empty");

        project.taskIds.remove(task.id);
        if (!project.taskIds.isEmpty()) project = this.fetchTasks(project);
        return project;
    }

    public List<Task> fetchDue(Long dueDays) throws IllegalStateException {
        return this.fetchDueByTeam(null, dueDays);
    }

    public List<Task> fetchDueByTeam(Team team, Long dueDays) throws IllegalStateException {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.add(Calendar.DATE, dueDays.intValue() * -1);

        DBObject query = new BasicDBObject();
        if (team != null)
            query.put("teamId", team.id);
        query.put("end", new BasicDBObject("$gte", start.getTime()).append("$lte", end.getTime()));

        return StreamSupport.stream(this.db.getCollection("projects").find(query).spliterator(), false)
                .map(it -> this.db.getConverter().read(Task.class, it))
                .collect(Collectors.toList());
    }
}
