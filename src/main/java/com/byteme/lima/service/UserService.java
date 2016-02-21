package com.byteme.lima.service;

import com.byteme.lima.domain.User;
import com.byteme.lima.util.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService extends AbstractService {

    public List<User> findAll() {
        return this.db.findAll(User.class, "users");
    }

    public List<User> findAllByIds(List<String> ids) {
        List<ObjectId> memberObjectId = ids.parallelStream()
                .map(ObjectId::new)
                .collect(Collectors.toList());
        DBObject query = QueryBuilder.start("_id").in(memberObjectId).get();

        return StreamSupport.stream(this.db.getCollection("users").find(query).spliterator(), false)
                .map(it -> this.db.getConverter().read(User.class, it))
                .collect(Collectors.toList());

    }

    public User findById(String id) {
        return this.db.getConverter().read(
                User.class,
                this.db.getCollection("users").findOne(new ObjectId(id))
        );
    }

    public User findByCode(String code) {
        return this.db.getConverter().read(
                User.class,
                this.db.getCollection("users").findOne(
                        new BasicDBObject("code", code)
                )
        );
    }

    public User findByEmail(String email) {
        return this.db.getConverter().read(
                User.class,
                this.db.getCollection("users").findOne(
                        new BasicDBObject("email", email)
                )
        );
    }

    public List<User> findAllByName(String name) {
        DBObject query = new BasicDBObject();
        query.put("name", name);

        return StreamSupport.stream(this.db.getCollection("users").find(query).spliterator(), false)
                .map(it -> this.db.getConverter().read(User.class, it))
                .collect(Collectors.toList());
    }

    public List<User> findAllByType(User.Type type) {
        DBObject query = new BasicDBObject();
        query.put("type", type.name());

        return StreamSupport.stream(this.db.getCollection("users").find(query).spliterator(), false)
                .map(it -> this.db.getConverter().read(User.class, it))
                .collect(Collectors.toList());
    }

    public User save(User user) {
        if (StringUtils.isBlank(user.code)) user.code = "User-" + UUID.randomUUID().toString();

        this.db.save(user, "users");
        return this.findByCode(user.getCode());
    }

    public void remove(User user) {
        this.db.remove(user, "users");
    }

    public void removeAll() {
        for (DBObject user: this.db.getCollection("users").find()) {
            this.db.remove(user, "users");
        }
    }
}
