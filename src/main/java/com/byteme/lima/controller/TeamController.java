package com.byteme.lima.controller;

import com.byteme.lima.domain.Team;
import com.byteme.lima.domain.User;
import com.byteme.lima.service.TeamService;
import com.byteme.lima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/rest/teams")
public class TeamController {

    @Autowired
    public TeamService teamService;

    @RequestMapping(
            value = "",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public List<Team> get(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name
    ) {
        if (id != null)     return Arrays.asList(this.teamService.findById(id));
        if (code != null)   return Arrays.asList(this.teamService.findByCode(code));
        if (name != null)   return this.teamService.findAllByName(name);

        return this.teamService.findAll();
    }

    @RequestMapping(
            value = "/{id}",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public Team get(@PathVariable String id) {
        return this.teamService.fetchMembers(this.teamService.findById(id));
    }

    @RequestMapping(
            value = "/{id}",
            method = DELETE,
            produces = APPLICATION_JSON_VALUE)
    public void delete(@PathVariable String id) {
        this.teamService.remove(
                this.teamService.findById(id)
        );
    }

    @RequestMapping(
            value = "/bootstrap",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public void bootstrap() throws IOException {
        this.teamService.bootstrap();
    }
}
