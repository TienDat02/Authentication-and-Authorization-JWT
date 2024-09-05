package com.exercise.Repository;

import com.exercise.Entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByUsername(String username);


     boolean existsByEmail(String email);

    Optional<Object> findByEmail(String email);
}