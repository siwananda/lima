package com.byteme.lima.controller;

import com.byteme.lima.domain.Project;
import com.byteme.lima.domain.Team;
import com.byteme.lima.service.ProjectService;
import com.byteme.lima.service.TeamService;
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
@RequestMapping("/rest/projects")
public class ProjectController {

    @Autowired
    public ProjectService projectService;

    @RequestMapping(
            value = "",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public List<Project> get(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Project.Status status
    ) {
        if (id != null)     return Arrays.asList(this.projectService.findById(id));
        if (code != null)   return Arrays.asList(this.projectService.findByCode(code));
        if (name != null)   return this.projectService.findAllByName(name);
        if (status != null) return this.projectService.findAllByStatus(status);

        return this.projectService.findAll();
    }

    @RequestMapping(
            value = "/{id}",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public Project get(@PathVariable String id) {
        return this.projectService.fetchTeam(this.projectService.findById(id));
    }

    @RequestMapping(
            value = "/{id}",
            method = DELETE,
            produces = APPLICATION_JSON_VALUE)
    public void delete(@PathVariable String id) {
        this.projectService.remove(
                this.projectService.findById(id)
        );
    }

    @RequestMapping(
            value = "/bootstrap",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public void bootstrap() throws IOException {
        this.projectService.bootstrap();
    }
}
