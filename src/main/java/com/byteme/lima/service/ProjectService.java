package com.byteme.lima.service;

import com.byteme.lima.domain.Project;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProjectService extends AbstractService {

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

    public List<Project> findByStatus(Project.Status status) {
        DBObject query = new BasicDBObject();
        query.put("status", status.name());

        return StreamSupport.stream(this.db.getCollection("projects").find(query).spliterator(), false)
                .map(it -> this.db.getConverter().read(Project.class, it))
                .collect(Collectors.toList());
    }

    public void remove(Project project) {
        this.db.remove(project, "projects");
    }

    public void save(Project project) {
        this.db.save(project, "projects");
    }

    public void removeAll() {
        for (DBObject project: this.db.getCollection("projects").find()) {
            this.db.remove(project, "projects");
        }
    }

    public void bootstrap() throws IOException {
        this.removeAll();

        List<Project> Projects = new ObjectMapper().readValue(this.resourceLoader.getResource("classpath:projects.json").getFile(), new TypeReference<List<Project>>() { });
        for (Project project: Projects) {
            this.save(project);
        }
    }

}
