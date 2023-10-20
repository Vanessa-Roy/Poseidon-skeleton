package com.nnk.springboot.security;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.enums.Role;
import com.nnk.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * The initialization of Authenticated user provider.
 */
@Service
public class AuthenticatedUserProvider {

    @Autowired
    UserService userService;

    /**
     * Gets authenticated user.
     *
     * @return the authenticated user
     */
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.loadUserByUsername(auth.getName());
    }

    /**
     * Gets user's role.
     *
     * @return true if the authenticated user has an admin role
     */
    public boolean isAdmin(User authenticatedUser) {
        return authenticatedUser.getRole().equals(Role.ADMIN);
    }
}