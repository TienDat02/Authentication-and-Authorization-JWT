package com.exercise.Controller;

import com.exercise.Model.MyUserDetailService;
import com.exercise.Webtoken.JwtService;
import com.exercise.Webtoken.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Controller // Change this from @Controller to @RestController
public class ContentController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MyUserDetailService myUserDetailService;

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

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.username(), loginForm.password()));
        if(authentication.isAuthenticated()) {
            String token = jwtService.generateToken(myUserDetailService.loadUserByUsername(loginForm.username()));
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            throw new UsernameNotFoundException("Invalid user credentials");
        }
    }
}