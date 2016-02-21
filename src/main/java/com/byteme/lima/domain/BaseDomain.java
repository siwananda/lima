package com.byteme.lima.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BaseDomain {
    @Id
    public String id;
    public String code;
    public String name;

    @Transient
    public List<Event> history;
    public List<String> historyIds;

    public void addEvent(Event event) {
        if (this.historyIds == null) this.historyIds = new ArrayList<>();
        this.historyIds.add(event.id);
    }
}
