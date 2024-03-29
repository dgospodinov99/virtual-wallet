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
    private static final String UNAUTHORIZED_ADMIN = "This resource requires admin rights!";
    private static final String UNAUTHORIZED_USER = "The requested resource requires authentication.";
    private final String AUTHORIZATION_HEADER_NAME = "Authorization";

    private final UserService userService;

    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,UNAUTHORIZED_USER);
        }
        String username = headers.getFirst(AUTHORIZATION_HEADER_NAME);
        if (username.isBlank()) {
            throw new AuthenticationFailureException(UNAUTHORIZED_ADMIN);
        }
        return userService.getByUsername(username);
    }

    public User tryGetUser(HttpSession session) {
        String currentUser = (String) session.getAttribute("currentUser");
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER);
        }

        return userService.getByUsername(currentUser);
    }

    public User tryGetAdmin(HttpSession session) {
        User user = tryGetUser(session);
        if (!user.isAdmin()) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_ADMIN);
        }

        return user;
    }


    public User tryGetAdmin(HttpHeaders headers) {
        User user = tryGetUser(headers);
        if (!user.isAdmin()) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_ADMIN);
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
