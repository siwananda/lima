package com.byteme.lima.controller;

import com.byteme.lima.domain.Project;
import com.byteme.lima.domain.Task;
import com.byteme.lima.domain.Team;
import com.byteme.lima.exception.IllegalStateException;
import com.byteme.lima.exception.NotFoundException;
import com.byteme.lima.service.ProjectService;
import com.byteme.lima.service.TaskService;
import com.byteme.lima.service.TeamService;
import com.byteme.lima.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/rest/projects")
public class ProjectController {

    @Autowired
    public ProjectService projectService;

    @Autowired
    public TaskService taskService;

    @Autowired
    public TeamService teamService;

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
        Project project = this.projectService.findById(id);
        project = this.projectService.fetchTasks(project);
        project = this.projectService.fetchTeam(project);
        return project;
    }

    @RequestMapping(
            value = "",
            method = POST,
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE
    )
    public Project post(@RequestBody Project project) throws IOException, IllegalStateException {
        if (StringUtils.isNotBlank(project.id)) throw new IllegalStateException("project.id is present");
        return this.projectService.save(project);
    }

    @RequestMapping(
            value = "/{id}",
            method = PUT,
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE
    )
    public Project post(@PathVariable String id, @RequestBody Project project) throws IOException {
        return this.projectService.save(project);
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
            value = "",
            method = DELETE,
            produces = APPLICATION_JSON_VALUE)
    public void deleteAll() {
        this.projectService.removeAll();
    }

    @RequestMapping(
            value = "{project}/tasks/{task}",
            method = PUT,
            produces = APPLICATION_JSON_VALUE)
    public Project addTask(
            @PathVariable String project,
            @PathVariable String task
    ) throws IllegalStateException, NotFoundException {
        Project _project = this.projectService.findById(project);
        if (_project == null) throw new NotFoundException("project not found with id: " + project);

        Task _task = this.taskService.findById(task);
        if (_task == null) throw new NotFoundException("task not found with id: " + task);

        return this.projectService.add(_project, _task);
    }

    @RequestMapping(
            value = "{project}/task/{task}",
            method = DELETE,
            produces = APPLICATION_JSON_VALUE)
    public Project removeTask(
            @PathVariable String project,
            @PathVariable String task
    ) throws IllegalStateException, NotFoundException {
        Project _project = this.projectService.findById(project);
        if (_project == null) throw new NotFoundException("project not found with id: " + project);

        Task _task = this.taskService.findById(task);
        if (_task == null) throw new NotFoundException("task not found with id: " + task);

        return this.projectService.remove(_project, _task);
    }

    @RequestMapping(
            value = "/due",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public List<Task> fetchDue() throws IllegalStateException, NotFoundException {
        return this.projectService.fetchDue(Constants.Dates.DUE_DAYS);
    }

    @RequestMapping(
            value = "/due/{team}",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public List<Task> fetchDue(@PathVariable String team) throws IllegalStateException, NotFoundException {
        Team _team = this.teamService.findById(team);
        if (_team == null) throw new NotFoundException("team not found with id: " + team);

        return this.projectService.fetchDueByTeam(_team, Constants.Dates.DUE_DAYS);
    }
}
