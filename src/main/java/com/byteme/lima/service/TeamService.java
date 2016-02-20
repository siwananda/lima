package com.byteme.lima.service;

import com.byteme.lima.domain.Team;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TeamService extends AbstractService {

    @Autowired
    public UserService userService;

    public List<Team> findAll() {
        return this.db.findAll(Team.class, "teams");
    }

    public Team findById(String id) {
        return this.db.getConverter().read(
                Team.class,
                this.db.getCollection("teams").findOne(new ObjectId(id))
        );
    }

    public Team findByCode(String code) {
        return this.db.getConverter().read(
                Team.class,
                this.db.getCollection("teams").findOne(
                        new BasicDBObject("code", code)
                )
        );
    }

    public List<Team> findAllByName(String name) {
        DBObject query = new BasicDBObject();
        query.put("name", name);

        return StreamSupport.stream(this.db.getCollection("teams").find(query).spliterator(), false)
                .map(it -> this.db.getConverter().read(Team.class, it))
                .collect(Collectors.toList());
    }

    public Team fetchMembers(Team team) {
        if (team.memberIds != null) {
            team.members = new ArrayList<>();
//            team.members.addAll(team.memberIds.stream()
//                    .map(memberId -> this.userService.findById(memberId))
//                    .collect(Collectors.toList()));
            team.members.addAll(this.userService.findAllByIds(team.memberIds));
        }
        return team;
    }

    public Team save(Team team) {
        this.db.save(team, "teams");
        return this.findByCode(team.getCode());
    }

    public void remove(Team team) {
        this.db.remove(team, "teams");
    }

    public void removeAll() {
        for (DBObject team: this.db.getCollection("teams").find()) {
            this.db.remove(team, "teams");
        }
    }
}
