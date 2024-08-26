package com.exercise.Model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    public String username;
    public String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    public MyUser() {
    }
    public MyUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Set<String> getRoles() {
        return roles;
    }
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    public void addRole(String role) {
        this.roles.add(role);
    }
    public void removeRole(String role) {
        this.roles.remove(role);
    }
}