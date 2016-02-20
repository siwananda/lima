package com.byteme.lima.service

import com.byteme.lima.domain.User
import com.byteme.lima.domain.User.Type
import com.byteme.lima.util.Constants
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.DBObject
import org.springframework.stereotype.Service

@Service
class UserService extends AbstractService {

    List<User> findAll() {
        [
                new User(
                        id: 1,
                        code: "code-001",
                        name: "name-001"
                ),
                new User(
                        id: 2,
                        code: "code-002",
                        name: "name-002"
                )
        ]
    }

    User findById(String id) {
        new User(
                id: "1",
                code: "admin-001",
                name: "Administrator",
                email: "admin@lima.com",
                type: Type.ADMIN
        )
    }

    User findByCode(String code) {
        new User(
                id: "1",
                code: code,
                name: "User with code [${code}]",
                email: "${code}@lima.com",
                type: Type.USER
        )
    }

    User findByEmail(String email) {
        new User(
                id: "1",
                code: "code-001",
                name: "name-001",
                email: email,
                type: Type.USER
        )
    }

    List<User> findAllByName(String name) {
        List<User> result = new ArrayList<>()
        for (number in 1..5) {
            result.add(
                    new User(
                            id: "${number}",
                            code: "code-${number}",
                            name: name,
                            email: "${name}@lima.com",
                            type: Type.USER
                    )
            )
        }

        result
    }

    List<User> findAllByType(Type type) {
        List<User> result = new ArrayList<>()
        for (number in 1..5) {
            result.add(
                    new User(
                            id: "${number}",
                            code: "admin-${number}",
                            name: "Administrator #${number}",
                            email: "admin.${number}@lima.com",
                            type: User.Type.ADMIN
                    )
            )
        }

        result
    }

    void save(User user) {
        this.db.save(user, "users")
    }

    void removeAll() {
        for (DBObject user: this.db.getCollection("users").find()) {
            this.db.remove(user, "users")
        }
    }

    void bootstrap() {
        this.removeAll()

        List<User> users = new ObjectMapper().readValue(Constants.Bootstrap.USERS, new TypeReference<List<User>>() { })
        for (user in users) {
            this.save(user)
        }
    }
}
