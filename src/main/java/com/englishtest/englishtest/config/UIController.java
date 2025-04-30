package com.englishtest.englishtest.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/log-out")
    public String logout() {
        return "logout";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/student/homework")
    public String studentDashboard() {
        return "student/mcq";
    }

    @GetMapping("/teacher/create-user")
    public String createUser() {
        return "teacher/create-user";
    }

    @GetMapping("/teacher/get-user")
    public String getUser() {
        return "teacher/get-user";
    }
}
