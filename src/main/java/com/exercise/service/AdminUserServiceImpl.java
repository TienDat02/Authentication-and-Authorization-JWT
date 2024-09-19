package com.exercise.service;

import com.exercise.entity.MyUser;
import com.exercise.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<MyUser> getAllUsers() {
        return myUserRepository.findAll();
    }

    @Override
    public MyUser addUser(MyUser user) {
        if (myUserRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return myUserRepository.save(user);
    }

    @Override
    public void deleteUser(long id) {
        myUserRepository.deleteById(id);
    }

    @Override
    public MyUser updateUser(MyUser user) {
        Optional<MyUser> existingUserOpt = myUserRepository.findById(user.getId());
        if (existingUserOpt.isPresent()) {
            MyUser existingUser = existingUserOpt.get();

            existingUser.setUsername(user.getUsername());
            existingUser.setRoles(user.getRoles());
            existingUser.setEmail(user.getEmail());
            existingUser.setPermissions(user.getPermissions());

            if (user.getPassword() != null && !user.getPassword().isEmpty() &&
                    !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            return myUserRepository.save(existingUser);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}