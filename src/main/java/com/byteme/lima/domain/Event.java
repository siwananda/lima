package com.byteme.lima.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "events")
public class Event extends BaseDomain {
    public Date timestamp;
    public String description;
}
