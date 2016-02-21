package com.byteme.lima.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "comments")
public class Comment extends BaseDomain {
    public Date timestamp;
    public String content;

    @Transient
    public User user;
    public String userId;
}
