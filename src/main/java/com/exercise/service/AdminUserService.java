package com.exercise.service;

import com.exercise.entity.MyUser;
import java.util.List;

public interface AdminUserService {
    List<MyUser> getAllUsers();
    MyUser addUser(MyUser user);
    void deleteUser(long id);
    MyUser updateUser(MyUser user);
}