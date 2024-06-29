package com.trinhducthang.ielts_test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login() {
        // Xử lý logic đăng nhập ở đây
        return "redirect:/dashboard"; // Chuyển hướng sau khi đăng nhập thành công
    }
}
