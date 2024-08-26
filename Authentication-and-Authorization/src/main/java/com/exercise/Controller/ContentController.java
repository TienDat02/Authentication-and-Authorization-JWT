package com.exercise.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContentController {

    @GetMapping("/home")
    public String handleHome() {
        return "home";
    }
    @GetMapping("/admin/home")
    public String handleAdminHome() {
        return "home_admin";
    }
    @GetMapping("/user/home")
    public String handleUserHome() {
        return "home_user";
    }
    @GetMapping("/login")
    public String handleLogin() {
        return "login";
    }
}
