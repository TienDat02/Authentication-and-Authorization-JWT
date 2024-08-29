package com.exercise.Controller;

import com.exercise.Model.MyUser;
import com.exercise.Model.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String confirmPassword) {
        // Basic validation
        if (!password.equals(confirmPassword)) {
            return "redirect:/register?error=passwordMismatch";
        }

        // Check if username already exists
        if (myUserRepository.findByUsername(username).isPresent()) {
            return "redirect:/register?error=userExists";
        }

        // Create and save the new user
        MyUser newUser = new MyUser(username, passwordEncoder.encode(password));
        newUser.addRole("USER");
        myUserRepository.save(newUser);

        return "redirect:/login?registered";
    }
}