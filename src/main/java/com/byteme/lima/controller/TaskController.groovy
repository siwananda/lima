package com.byteme.lima.controller

import com.byteme.lima.domain.Task
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.web.bind.annotation.RequestMethod.GET

@RestController
@RequestMapping("/rest/tasks")
class TaskController {

    @RequestMapping(
            value = "/test",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    Task test() {
        new Task(
                id: 1,
                code: "code-000",
                name: "name-000",
                description: "this is the description"
        )
    }
}