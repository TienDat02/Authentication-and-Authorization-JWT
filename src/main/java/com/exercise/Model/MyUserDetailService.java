package com.exercise.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private MyUserRepository myUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> myUser = myUserRepository.findByUsername(username);
        if(myUser.isPresent()){
            var userObj = myUser.get();
            List<GrantedAuthority> authorities = userObj.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(
                    userObj.getUsername(),
                    userObj.getPassword(),
                    authorities
            );
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}