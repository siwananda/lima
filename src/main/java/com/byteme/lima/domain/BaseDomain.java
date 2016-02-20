package com.byteme.lima.domain;

import org.springframework.data.annotation.Id;

public class BaseDomain {
    @Id
    public String id;
    public String code;
    public String name;
}
