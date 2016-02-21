package com.byteme.lima.controller;

import com.byteme.lima.domain.Project;
import com.byteme.lima.domain.Task;
import com.byteme.lima.domain.User;
import com.byteme.lima.exception.IllegalStateException;
import com.byteme.lima.exception.NotFoundException;
import com.byteme.lima.service.TaskService;
import com.byteme.lima.service.UserService;
import com.byteme.lima.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/rest/tasks")
public class TaskController {

    @Autowired
    public TaskService taskService;

    @Autowired
    public UserService userService;

    @RequestMapping(
            value = "",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public List<Task> get(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String user
    ) {
        if (id != null)     return Arrays.asList(this.taskService.findById(id));
        if (code != null)   return Arrays.asList(this.taskService.findByCode(code));
        if (user != null)   return this.taskService.findAllByAssignee(user);

        return this.taskService.findAll();
    }

    @RequestMapping(
            value = "/{id}",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public Task get(@PathVariable String id) {
        return this.taskService.findById(id);
    }

    @RequestMapping(
            value = "/{id}",
            method = DELETE,
            produces = APPLICATION_JSON_VALUE)
    public void delete(@PathVariable String id) {
        this.taskService.remove(
                this.taskService.findById(id)
        );
    }

    @RequestMapping(
            value = "",
            method = POST,
            produces = APPLICATION_JSON_VALUE
    )
    public Task post(@RequestBody Task task) throws IOException {
        return this.taskService.save(task);
    }

    @RequestMapping(
            value = "{task}/user/{user}",
            method = PUT,
            produces = APPLICATION_JSON_VALUE)
    public Task assign(
            @PathVariable String task,
            @PathVariable String user
    ) throws IllegalStateException, NotFoundException {
        Task _task = this.taskService.findById(task);
        if (_task == null) throw new NotFoundException("task not found with id: " + task);

        User _user = this.userService.findById(user);
        if (_user == null) throw new NotFoundException("user not found with id: " + user);

        return this.taskService.add(_task, _user);
    }

    @RequestMapping(
            value = "{task}/user/{user}",
            method = DELETE,
            produces = APPLICATION_JSON_VALUE)
    public Task unAssign(
            @PathVariable String task,
            @PathVariable String user
    ) throws IllegalStateException, NotFoundException {
        Task _task = this.taskService.findById(task);
        if (_task == null) throw new NotFoundException("task not found with id: " + task);

        User _user = this.userService.findById(user);
        if (_user == null) throw new NotFoundException("user not found with id: " + user);

        return this.taskService.remove(_task, _user);
    }

    @RequestMapping(
            value = "/due",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public List<Task> fetchDue() throws IllegalStateException, NotFoundException {
        return this.taskService.fetchDue(Constants.Dates.DUE_DAYS);
    }

    @RequestMapping(
            value = "/due/{user}",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public List<Task> fetchDue(@PathVariable String user) throws IllegalStateException, NotFoundException {
        User _user = this.userService.findById(user);
        if (_user == null) throw new NotFoundException("user not found with id: " + user);

        return this.taskService.fetchDueByUser(_user, Constants.Dates.DUE_DAYS);
    }
}
