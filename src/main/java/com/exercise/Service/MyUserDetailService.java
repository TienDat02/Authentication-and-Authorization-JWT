package com.exercise.Service;

import com.exercise.Entity.MyUser;
import com.exercise.Repository.MyUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private MyUserRepository myUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> myUser = myUserRepository.findByUsername(username);
        if(myUser.isPresent()){
            var userObj = myUser.get();
            List<GrantedAuthority> authorities = new ArrayList<>();
            
            // Add role-based authorities
            authorities.addAll(userObj.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList()));
            
            // Add permission-based authorities
            authorities.addAll(userObj.getPermissions().stream()
                    .map(permission -> new SimpleGrantedAuthority("PERMISSION_" + permission.toUpperCase()))
                    .collect(Collectors.toList()));
            
            // Add null checks and default values
            String userUsername = (userObj.getUsername() != null && !userObj.getUsername().isEmpty()) ? userObj.getUsername() : "defaultUsername";
            String userPassword = (userObj.getPassword() != null && !userObj.getPassword().isEmpty()) ? userObj.getPassword() : "defaultPassword";
            
            // Ensure authorities is not empty
            if (authorities.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            return new org.springframework.security.core.userdetails.User(
                    userUsername,
                    userPassword,
                    authorities

            );

        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }


}