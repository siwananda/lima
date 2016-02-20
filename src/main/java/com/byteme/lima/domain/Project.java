package com.byteme.lima.domain;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "projects")
public class Project extends BaseDomain {
    public Integer number;
    public String description;

    public Date start;
    public Date end;

    public Status status;

    @Transient
    public Team team;
    public String teamId;

    List<Task> tasks;

    enum Status {
        BACKLOG,
        IN_PROGRESS,
        DONE
    }
}
