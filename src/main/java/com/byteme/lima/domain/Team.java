package com.byteme.lima.domain;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "teams")
public class Team extends BaseDomain {
    @Transient
    public List<User> members;
    public List<String> memberIds;
}
