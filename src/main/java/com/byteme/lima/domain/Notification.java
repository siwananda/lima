package com.byteme.lima.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "notifications")
public class Notification extends BaseDomain {

    public String title;
    public String content;

    public Date date;
    public Status status;

    public enum Status {
        UNREAD,
        READ
    }
}
