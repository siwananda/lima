package com.byteme.lima.service;

import com.byteme.lima.domain.Notification;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService extends AbstractService {

    public List<Notification> findAll() {
        return this.db.findAll(Notification.class, "notifications");
    }

    public Notification findById(String id) {
        return this.db.getConverter().read(
                Notification.class,
                this.db.getCollection("notifications").findOne(new ObjectId(id))
        );
    }

    public Notification findByCode(String code) {
        return this.db.getConverter().read(
                Notification.class,
                this.db.getCollection("notifications").findOne(
                        new BasicDBObject("code", code)
                )
        );
    }

    public Notification findByTitle(String title) {
        return this.db.getConverter().read(
                Notification.class,
                this.db.getCollection("notifications").findOne(
                        new BasicDBObject("title", title)
                )
        );
    }
}
