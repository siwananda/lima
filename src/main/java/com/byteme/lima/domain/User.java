package com.byteme.lima.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "users")
public class User extends BaseDomain {
    public String email;
    public String avatar;
    public Type type;

    public enum Type {
        ADMIN,
        USER
    }
}
