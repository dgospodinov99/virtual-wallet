package com.team01.web.virtualwallet.controllers;

import com.team01.web.virtualwallet.exceptions.AuthenticationFailureException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.exceptions.UnauthorizedOperationException;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.services.contracts.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

@Component
public class AuthenticationHelper {

    private static final String WRONG_USERNAME_OR_PASSWORD = "Wrong username or password.";
    private static final String UNAUTHORIZED = "This resource requires admin rights!";
    private final String AUTHORIZATION_HEADER_NAME = "Authorization";

    private final UserService userService;

    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new AuthenticationFailureException("The requested resource requires authentication.");
        }
        String username = headers.getFirst(AUTHORIZATION_HEADER_NAME);
        if (username.isBlank()) {
            throw new AuthenticationFailureException("The requested resource requires administrator rights.");
        }
        return userService.getByUsername(username);
    }

    public User tryGetUser(HttpSession session) {
        String currentUser = (String) session.getAttribute("currentUser");
        if (currentUser == null) {
            throw new AuthenticationFailureException(UNAUTHORIZED);
        }

        return userService.getByUsername(currentUser);
    }

    public User tryGetAdmin(HttpHeaders headers) {
        User user = tryGetUser(headers);
        if (!user.isAdmin()) {
            throw new UnauthorizedOperationException(UNAUTHORIZED);
        }

        return user;
    }

    public User verifyAuthentication(String username, String password) {
        try {
            User user = userService.getByUsername(username);
            if (!user.getPassword().equals(password)) {
                throw new AuthenticationFailureException(WRONG_USERNAME_OR_PASSWORD);
            }
            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthenticationFailureException(WRONG_USERNAME_OR_PASSWORD);
        }
    }
}
