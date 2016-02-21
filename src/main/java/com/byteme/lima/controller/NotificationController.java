package com.byteme.lima.controller;

import com.byteme.lima.domain.Notification;
import com.byteme.lima.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/rest/notifications")
public class NotificationController {

    @Autowired
    public NotificationService notificationService;

    @RequestMapping(
            value = "",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public List<Notification> get(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String code
    ) {
        if (id != null)     return Arrays.asList(this.notificationService.findById(id));
        if (code != null)   return Arrays.asList(this.notificationService.findByCode(code));

        return this.notificationService.findAll();
    }


}
