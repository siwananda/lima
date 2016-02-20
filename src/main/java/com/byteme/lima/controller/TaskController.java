package com.byteme.lima.controller;

import com.byteme.lima.domain.Task;
import com.byteme.lima.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/rest/tasks")
public class TaskController {

    @Autowired
    public TaskService taskService;

    @RequestMapping(
            value = "",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public List<Task> get(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String code
    ) {
        if (id != null)     return Arrays.asList(this.taskService.findById(id));
        if (code != null)   return Arrays.asList(this.taskService.findByCode(code));

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

}
