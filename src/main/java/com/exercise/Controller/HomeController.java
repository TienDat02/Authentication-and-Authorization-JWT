package com.exercise.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String handleHome() {
        return "home";
    }

    @GetMapping("/admin/home")
    public String handleAdminHome() {
        return "admin-home";
    }

    @GetMapping("/login")
    public String handleLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String handleRegister() {
        return "register";
    }
}