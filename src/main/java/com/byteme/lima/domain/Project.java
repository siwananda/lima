package com.byteme.lima.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "projects")
public class Project extends BaseDomain {
    public String description;
    public Date start;
    public Date end;
    public Status status;

    @Transient
    public Team team;
    public String teamId;

    @Transient
    public List<Task> tasks;
    public List<String> taskIds;

    @Transient
    public List<Event> history;
    public List<String> historyIds;

    public enum Status {
        BACKLOG,
        IN_PROGRESS,
        DONE
    }
}
