package com.exercise.controller;

import com.exercise.entity.MyUser;
import com.exercise.service.AdminUserService;
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

    private final AdminUserService adminUserService;

    @Autowired
    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping("/user-management")
    public String handleAdminUserManagement(Model model) {
        List<MyUser> users = adminUserService.getAllUsers();
        model.addAttribute("users", users);
        return "admin-user-management";
    }

    @PostMapping("/add-account")
    @ResponseBody
    public ResponseEntity<?> addAccount(@RequestBody MyUser user) {
        try {
            MyUser savedUser = adminUserService.addUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-account")
    @ResponseBody
    public ResponseEntity<?> deleteAccount(@RequestParam long id) {
        adminUserService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }

    @PutMapping("/update-account")
    @ResponseBody
    public ResponseEntity<?> updateAccount(@RequestBody MyUser user) {
        try {
            MyUser updatedUser = adminUserService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users")
    @ResponseBody
    public List<MyUser> getUsers() {
        return adminUserService.getAllUsers();
    }
}