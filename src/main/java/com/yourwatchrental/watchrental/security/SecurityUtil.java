package com.yourwatchrental.watchrental.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityUtil {

    public boolean isAdmin() {
        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        if(authentication == null) return false;

        return authentication.getAuthorities()
                .stream()
                .anyMatch(
                        authority -> "ROLE_ADMIN".equals(authority.getAuthority())
                );
    }

    public UUID getCurrentUserId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }

        return UUID.fromString(authentication.getName());
    }
}
