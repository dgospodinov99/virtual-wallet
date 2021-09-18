package com.team01.web.virtualwallet.controllers.MVC;


import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.exceptions.AuthenticationFailureException;
import com.team01.web.virtualwallet.exceptions.DuplicateEntityException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.exceptions.InvalidPasswordException;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.LoginDto;
import com.team01.web.virtualwallet.models.dto.RegisterDto;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private static final String PASSWORDS_DONT_MATCH = "Password confirmation should match password.";

    private final AuthenticationHelper authenticationHelper;
    private final UserModelMapper modelMapper;
    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationHelper authenticationHelper, UserModelMapper modelMapper, UserService userService) {

        this.authenticationHelper = authenticationHelper;
        this.modelMapper = modelMapper;
        this.userService = userService;
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
            session.setAttribute("currentUserIsAdmin", user.isAdmin()); //sets user role
            return "redirect:/";
        } catch (AuthenticationFailureException | EntityNotFoundException e) {
            bindingResult.rejectValue("password", "auth_error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/";
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
            bindingResult.rejectValue("passwordConfirm", "password_error", PASSWORDS_DONT_MATCH);
            return "register";
        }

        try {
            User user = modelMapper.fromDto(register);
            userService.create(user);
            return "redirect:/auth/login";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "register";
        } catch (InvalidPasswordException e) {
            bindingResult.rejectValue("password", "password_error", e.getMessage());
            return "register";
        }
    }
}