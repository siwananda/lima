package com.byteme.lima.service;

import com.byteme.lima.domain.Project;
import com.byteme.lima.domain.Task;
import com.byteme.lima.domain.Team;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProjectService extends AbstractService {

    @Autowired
    public TeamService teamService;

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

    public void bootstrap() throws IOException {
        this.removeAll();

        List<BasicDBObject> items = new ObjectMapper().readValue(this.resourceLoader.getResource("classpath:projects.json").getFile(), new TypeReference<List<BasicDBObject>>() {});
        for (BasicDBObject item: items) {
            item.put("_id", new ObjectId(item.get("id").toString()));
            item.remove("id");
            this.db.save(item, "projects");
        }
    }
}
