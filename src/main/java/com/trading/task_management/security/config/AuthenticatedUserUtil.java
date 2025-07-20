package com.trading.task_management.security.config;

import com.trading.task_management.security.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserUtil {

    public User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User user) {
            return user;  // Return directly from SecurityContext
        }
        throw new RuntimeException("Invalid authentication");
    }

    public UUID getAuthenticatedUserId() {
        return getAuthenticatedUser().getId(); // No extra DB query
    }

    public User getCurrentUser() {
        return getAuthenticatedUser();
    }
}
