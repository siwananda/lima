package com.byteme.lima.domain

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = 'users')
class User extends BaseDomain {
    String email
    Type type

    enum Type {
        ADMIN,
        USER,
        GUEST
    }
}
