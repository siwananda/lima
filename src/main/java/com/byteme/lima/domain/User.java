package com.byteme.lima.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User extends BaseDomain {
    public String email;
    public Type type;

    public enum Type {
        ADMIN,
        USER,
        GUEST
    }
}
