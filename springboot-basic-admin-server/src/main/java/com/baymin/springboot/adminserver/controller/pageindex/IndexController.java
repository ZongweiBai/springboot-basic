package com.baymin.springboot.adminserver.controller.pageindex;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping
public class IndexController {

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        return "login";
    }

    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request) {
        return "welcome";
    }

    @GetMapping("/nologin")
    public String noLogin(HttpServletRequest request) {
        return "nologin";
    }

}
