package com.byteme.lima.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "projects")
public class Project extends BaseDomain {
    public String description;
    public Date start;
    public Date end;

    @Transient
    public Team team;
    public String teamId;
}
