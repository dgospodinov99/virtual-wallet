package com.team01.web.virtualwallet.controllers.MVC;


import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.exceptions.AuthenticationFailureException;
import com.team01.web.virtualwallet.exceptions.DuplicateEntityException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.exceptions.InvalidPasswordException;
import com.team01.web.virtualwallet.models.Token;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.LoginDto;
import com.team01.web.virtualwallet.models.dto.RegisterDto;
import com.team01.web.virtualwallet.models.dto.UserDto;
import com.team01.web.virtualwallet.services.contracts.EmailService;
import com.team01.web.virtualwallet.services.contracts.TokenService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private static final String PASSWORDS_DONT_MATCH = "Passwords do not match!.";

    private final AuthenticationHelper authenticationHelper;
    private final UserModelMapper modelMapper;
    private final UserService userService;
    private final TokenService tokenService;
    private final EmailService emailService;

    @Autowired
    public AuthenticationController(AuthenticationHelper authenticationHelper, UserModelMapper modelMapper, UserService userService, TokenService tokenService, EmailService emailService) {

        this.authenticationHelper = authenticationHelper;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto login,
                              BindingResult bindingResult,
                              HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            User user = authenticationHelper.verifyAuthentication(login.getUsername(), login.getPassword());
            session.setAttribute("currentUser", login.getUsername());
            session.setAttribute("currentUserImage", user.getPhotoName());
            return "redirect:/";
        } catch (AuthenticationFailureException | EntityNotFoundException e) {
            bindingResult.rejectValue("password", "auth_error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/auth/login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDto());
        return "register";
    }


    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto register,
                                 BindingResult bindingResult,
                                 HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (!register.getPassword().equals(register.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password_error", PASSWORDS_DONT_MATCH);
            return "register";
        }

        try {
            User user = modelMapper.fromDto(register);
            user.setBlocked(true);
            userService.create(user);
            emailService.sendVerifyRegistrationEmail(user.getEmail());
            return "redirect:/auth/login";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "register";
        } catch (InvalidPasswordException e) {
            bindingResult.rejectValue("password", "password_error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/verify/{token}")
    public String showVerificationPage(@PathVariable String token, Model model, HttpSession session) {
        try {

            User user = authenticationHelper.tryGetUser(session);
            Token toVerify = tokenService.getByToken(token);
            user.setBlocked(false);
            tokenService.delete(toVerify.getId());
            userService.update(user);
        } catch (AuthenticationFailureException | EntityNotFoundException e) {
            return "error-login-first";
        }
        return "verify";
    }
}