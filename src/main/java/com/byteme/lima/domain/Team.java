package com.byteme.lima.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "teams")
public class Team extends BaseDomain {
    @Transient
    public List<User> members;
    public List<String> memberIds;
}
