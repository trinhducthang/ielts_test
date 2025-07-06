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

    @GetMapping("/student/my-reading")
    public String readingTest2() {
        return "student/my-reading";
    }

    @GetMapping("/student/submit-reading")
    public String submitReading() {
        return "student/submit-reading";
    }


    @GetMapping("/teacher/reading-results")
    public String readingResult() {
        return "teacher/reading-result";
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

    @GetMapping("/teacher/get-answer")
    public String getAnswer() {
        return "teacher/get-answer";
    }

    @GetMapping("/student/my-listening")
    public String listening() {
        return "student/my-listening";
    }

    @GetMapping("/student/submit-listening")
    public String submitListening() {
        return "student/submit-listening";
    }

    @GetMapping("/teacher/listening")
    public String listening2() {
        return "teacher/listening_assign";
    }

    @GetMapping("/teacher/listening-result")
    public String listeningResult() {
        return "teacher/listening-result";
    }

    @GetMapping("/student/my-speaking")
    public String mySpeaking() {
        return "student/my-speaking";
    }

    @GetMapping("student/submit-speaking")
    public String submitSpeaking() {
        return "student/submit-speaking";
    }

    @GetMapping("/student/speaking-submitted")
    public String speakingSubmitted() {
        return "student/speaking-submitted";
    }

    @GetMapping("/teacher/create-speaking")
    public String createSpeaking() {
        return "teacher/speaking-assign";
    }

    @GetMapping("/teacher/give-feedback-speaking")
    public String giveFeedbackSpeaking() {
        return "teacher/give-feedback-speaking";
    }

    @GetMapping("/message")
    public String message() {
        return "/student/message";
    }


}
