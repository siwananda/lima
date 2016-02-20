package com.byteme.lima.controller

import com.byteme.lima.domain.Task
import com.byteme.lima.domain.User
import com.byteme.lima.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.web.bind.annotation.RequestMethod.GET

@RestController
@RequestMapping("/rest/users")
class UserController {

    @Autowired
    UserService userService

    @RequestMapping(
            value = "",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    List<User> test() {
        this.userService.findAll()
    }
}
