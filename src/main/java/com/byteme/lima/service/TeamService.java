package com.byteme.lima.service;

import com.byteme.lima.domain.Team;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService extends AbstractService {

    public List<Team> findAll() {
        return this.db.findAll(Team.class, "teams");
    }

    public Team findById(String id) {
        return this.db.getConverter().read(
                Team.class,
                this.db.getCollection("teams").findOne(new ObjectId(id))
        );
    }
}
