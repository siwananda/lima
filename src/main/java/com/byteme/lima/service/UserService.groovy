package com.byteme.lima.service

import com.byteme.lima.domain.User
import com.byteme.lima.domain.User.Type
import com.byteme.lima.util.Constants
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

import java.util.stream.StreamSupport

@Service
class UserService extends AbstractService {

    List<User> findAll() {
        this.db.findAll(User.class, "users")
    }

    User findById(String id) {
        this.db.getConverter().read(
                User.class,
                this.db.getCollection("users").findOne(new ObjectId(id))
        )
    }

    User findByCode(String code) {
        this.db.getConverter().read(
                User.class,
                this.db.getCollection("users").findOne(
                        new BasicDBObject("code", code)
                )
        )
    }

    User findByEmail(String email) {
        this.db.getConverter().read(
                User.class,
                this.db.getCollection("users").findOne(
                        new BasicDBObject("email", email)
                )
        )
    }

    List<User> findAllByName(String name) {
        List<User> result = new ArrayList<>()

        DBObject query = new BasicDBObject()
        query.put("name", name)

        this.db.getCollection("users").find(query).asCollection().forEach(
                {
                    result.add(this.db.getConverter().read(User.class, it))
                }
        )

        result
    }

    List<User> findAllByType(Type type) {
        List<User> result = new ArrayList<>()

        DBObject query = new BasicDBObject()
        query.put("type", type.name())

        this.db.getCollection("users").find(query).asCollection().forEach(
                {
                    result.add(this.db.getConverter().read(User.class, it))
                }
        )

        result
    }

    void save(User user) {
        this.db.save(user, "users")
    }

    void remove(User user) {
        this.db.remove(user, "users")
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
