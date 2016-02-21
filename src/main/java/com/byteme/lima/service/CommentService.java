package com.byteme.lima.service;

import com.byteme.lima.domain.*;
import com.byteme.lima.exception.IllegalStateException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CommentService extends AbstractService {

    @Autowired
    public BootstrapService bootstrapService;

    @Autowired
    public ProjectService projectService;

    @Autowired
    public TaskService taskService;

    @Autowired
    public TeamService teamService;

    @Autowired
    public UserService userService;

    @Autowired
    public EventService eventService;

    public List<Comment> findAllByIds(List<String> ids) {
        if (ids != null && !CollectionUtils.isEmpty(ids)) {
            List<ObjectId> objectIds = ids.parallelStream()
                    .map(ObjectId::new)
                    .collect(Collectors.toList());

            DBObject query = QueryBuilder.start("_id").in(objectIds).get();

            return StreamSupport.stream(this.db.getCollection("comments").find(query).spliterator(), true)
                    .map(it -> this.db.getConverter().read(Comment.class, it))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<Comment> findByProject(Project project) {
        if (project != null && project.commentIds != null && !CollectionUtils.isEmpty(project.commentIds)) {
            project.comments = new ArrayList<>();
            project.comments.addAll(this.findAllByIds(project.commentIds));
            return project.comments;
        }

        return null;
    }

    public List<Comment> findByUser(User user) {
        if (user != null && user.id != null) {
            DBObject query = new BasicDBObject();
            query.put("userId", user.id);

            return StreamSupport.stream(this.db.getCollection("comments").find(query).spliterator(), false)
                    .map(it -> this.db.getConverter().read(Comment.class, it))
                    .collect(Collectors.toList());
        }

        return null;
    }

    public Comment findByCode(String code) {
        return this.db.getConverter().read(
                Comment.class,
                this.db.getCollection("comments").findOne(
                        new BasicDBObject("code", code)
                )
        );
    }

    public Comment comment(Project project, User user, String commentText) throws IllegalStateException {
        if (project == null || project.id == null || StringUtils.isBlank(project.id)) throw new IllegalStateException("project is empty");
        if (user == null    || user.id == null    || StringUtils.isBlank(user.id))    throw new IllegalStateException("user is empty");
        if (StringUtils.isBlank(commentText)) throw new IllegalStateException("comment is empty");

        Integer id = this.bootstrapService.next();
        Comment comment = new Comment();
        comment.code = "Comment-" + id;
        comment.timestamp = new Date();
        comment.userId = user.id;

        this.db.save(comment, "comments");
        comment = this.findByCode(comment.getCode());

        this.eventService.createEvent(user, " comments on ", project);
        this.eventService.createEvent(project, " commented by ", user);

        return comment;
    }
}
