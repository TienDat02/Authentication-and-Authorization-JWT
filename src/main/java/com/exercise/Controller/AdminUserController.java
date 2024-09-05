package com.exercise.Controller;

import com.exercise.Entity.MyUser;
import com.exercise.Repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user-management")
    public String handleAdminUserManagement(Model model) {
        List<MyUser> users = myUserRepository.findAll();
        model.addAttribute("users", users);
        return "admin-user-management";
    }

    @PostMapping("/add-account")
    @ResponseBody
    public ResponseEntity<?> addAccount(@RequestBody MyUser user) {
        if (myUserRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        MyUser savedUser = myUserRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/delete-account")
    @ResponseBody
    public ResponseEntity<?> deleteAccount(@RequestParam long id) {
        myUserRepository.deleteById(id);
        return ResponseEntity.ok("User deleted");
    }

    @PutMapping("/update-account")
    @ResponseBody
    public ResponseEntity<?> updateAccount(@RequestBody MyUser user) {
        Optional<MyUser> existingUserOpt = myUserRepository.findById(user.getId());
        if (existingUserOpt.isPresent()) {
            MyUser existingUser = existingUserOpt.get();

            // Update fields
            existingUser.setUsername(user.getUsername());
            existingUser.setRoles(user.getRoles());
            existingUser.setEmail(user.getEmail());
            existingUser.setPermissions(user.getPermissions());

            // Only update password if it's provided and different
            if (user.getPassword() != null && !user.getPassword().isEmpty() &&
                    !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            MyUser updatedUser = myUserRepository.save(existingUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users")
    @ResponseBody
    public List<MyUser> getUsers() {
        return myUserRepository.findAll();
    }
}