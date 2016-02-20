package com.byteme.lima.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "tasks")
public class Task extends BaseDomain {
    public String description;

    public Date start;
    public Date end;

    @Transient
    public User assignee;
    public String assigneeId;

    public Status status;

    public enum Status {
        BACKLOG,
        IN_PROGRESS,
        DONE
    }
}
