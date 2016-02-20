package com.byteme.lima.controller

import com.byteme.lima.domain.Task
import com.byteme.lima.domain.User
import com.byteme.lima.domain.User.Type
import com.byteme.lima.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
    List<User> get(
            @RequestParam(required = false) Number id,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Type type
    ) {
        if (id)     return [this.userService.findById(id)]
        if (code)   return [this.userService.findByCode(code)]
        if (name)   return [this.userService.findAllByName(name)]
        if (email)  return [this.userService.findByEmail(email)]
        if (type)   return [this.userService.findAllByType()]

        this.userService.findAll()
    }

    @RequestMapping(
            value = "/{id}",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    User get(@PathVariable Number id) {
        this.userService.findById(id)
    }
}
