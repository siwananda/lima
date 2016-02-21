package com.byteme.lima.controller;

import com.byteme.lima.domain.*;
import com.byteme.lima.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/rest/events")
public class EventController extends AbstractService {

    @Autowired
    public EventService eventService;

    @Autowired
    public TaskService taskService;

    @Autowired
    public ProjectService projectService;

    @Autowired
    public TeamService teamService;

    @Autowired
    public UserService userService;

    @RequestMapping(
            value = "/{id}",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public List<Event> get(@PathVariable String id) {
        List<Event> events = new ArrayList<>();

        Task task = taskService.findById(id);
        if (task != null) events = eventService.findByTask(task);

        Project project = projectService.findById(id);
        if (project != null) events = eventService.findByProject(project);

        Team team = teamService.findById(id);
        if (team != null) events = eventService.findByTeam(team);

        User user = userService.findById(id);
        if (user != null) events = eventService.findByUser(user);

        return events;
    }

}
