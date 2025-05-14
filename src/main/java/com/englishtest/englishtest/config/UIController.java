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

    @GetMapping("/teacher/assigment")
    public String index() {
        return "teacher/assignment";
    }

    @GetMapping("/student/answer/reading")
    public String studentDashboard() {
        return "/student/answer";
    }


    @GetMapping("/teacher/user-management")
    public String getUser() {
        return "teacher/user-management";
    }

    @GetMapping("/teacher/reading-test")
    public String readingTest() {
        return "teacher/reading-test";
    }

    @GetMapping("/teacher/course-management")
    public String courseManagement() {
        return "teacher/course";
    }

    @GetMapping("/student/reading-test")
    public String readingTest2() {
        return "student/reading-test";
    }

    @GetMapping("/student/home")
    public String home() {
        return "student/home";
    }

    @GetMapping("/student/my-writing")
    public String myWriting() {
        return "student/my-writing";
    }

    @GetMapping("/student/submit-writing")
    public String submitWriting() {
        return "student/submit-writing";
    }

    @GetMapping("/teacher/create-writing")
    public String createWriting() {
        return "teacher/create-writing";
    }

    @GetMapping("/teacher/give-feedback")
    public String giveFeedback() {
        return "teacher/give-feedback";
    }

    @GetMapping("/student/writing-submitted")
    public String writing() {
        return "student/writing-submitted";
    }
}
