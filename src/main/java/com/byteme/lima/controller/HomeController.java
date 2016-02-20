package com.byteme.lima.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author siwananda
 * @since 2/20/2016.
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @RequestMapping("")
    public String home(){
        return "index";
    }
}
