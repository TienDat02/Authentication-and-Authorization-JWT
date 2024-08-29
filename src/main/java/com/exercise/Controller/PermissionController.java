package com.exercise.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PermissionController {

    @PostMapping("/api/check-permission")
    public Map<String, Boolean> checkPermission(@RequestBody Map<String, String> body, Authentication authentication) {
        String requiredPermission = "PERMISSION_" + body.get("permission");
        boolean hasPermission = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(requiredPermission));
        return Map.of("hasPermission", hasPermission);
    }
}