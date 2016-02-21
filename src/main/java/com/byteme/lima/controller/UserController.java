package com.byteme.lima.controller;

import com.byteme.lima.domain.User;
import com.byteme.lima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/rest/users")
public class UserController {

    @Autowired
    public UserService userService;

    @RequestMapping(
            value = "",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public List<User> get(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) User.Type type
    ) {
        if (id != null)     return Arrays.asList(this.userService.findById(id));
        if (code != null)   return Arrays.asList(this.userService.findByCode(code));
        if (name != null)   return this.userService.findAllByName(name);
        if (email != null)  return Arrays.asList(this.userService.findByEmail(email));
        if (type != null)   return this.userService.findAllByType(type);

        return this.userService.findAll();
    }

    @RequestMapping(
            value = "/{id}",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public User get(@PathVariable String id) {
        return this.userService.findById(id);
    }

    @RequestMapping(
            value = "/{id}",
            method = DELETE,
            produces = APPLICATION_JSON_VALUE)
    public void delete(@PathVariable String id) {
        this.userService.remove(
                this.userService.findById(id)
        );
    }

    @RequestMapping(
            value = "",
            method = DELETE,
            produces = APPLICATION_JSON_VALUE)
    public void deleteAll() {
        this.userService.removeAll();
    }

    @RequestMapping(
            value = "",
            method = POST,
            produces = APPLICATION_JSON_VALUE
    )
    public void post(@RequestBody User user) throws IOException {
        this.userService.save(user);
    }

}
