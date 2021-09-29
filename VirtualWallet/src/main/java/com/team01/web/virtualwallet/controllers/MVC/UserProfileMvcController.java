package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.exceptions.*;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.ChangePasswordDto;
import com.team01.web.virtualwallet.models.dto.UpdateUserDto;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/myaccount")
public class UserProfileMvcController {

    private static final String INVALID_OLD_PASSWORD = "Invalid old Password!";
    private static final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match!";
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserModelMapper modelMapper;

    @Autowired
    public UserProfileMvcController(UserService userService, AuthenticationHelper authenticationHelper, UserModelMapper modelMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        return authenticationHelper.tryGetUser(session).isAdmin();
    }


    @ModelAttribute("user")
    public User populateUser(HttpSession session) {
        try {
            return authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping
    public String showUserProfile() {
        return "profile-user";
    }

    @GetMapping("/update")
    public String showEditPage(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (UnauthorizedOperationException | AuthenticationFailureException e) {
            return "error401";
        }

        try {
            User user = populateUser(session);
            UpdateUserDto dto = modelMapper.toUpdateDto(user);
            model.addAttribute("dto", dto);
            return "profile-user-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errors", e.getMessage());
            return "error404";
        }
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("dto") UpdateUserDto dto,
                             BindingResult errors,
                             Model model,
                             HttpSession session) {
        User executor;
        try {
            executor = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "error401";
        }
        if (errors.hasErrors()) {
            return "profile-user-update";
        }

        try {
            User user = modelMapper.fromDto(dto, populateUser(session).getId());
            userService.update(user);

            return "redirect:/myaccount";
        } catch (UnauthorizedOperationException e) {
            return "error401";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("email", "duplicate", e.getMessage());
            return "profile-user-update";
        }
    }

    @GetMapping("/update/password")
    public String showEditPasswordPage(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (UnauthorizedOperationException | AuthenticationFailureException e) {
            return "error401";
        }

        try {
            User user = populateUser(session);
            model.addAttribute("dto", new ChangePasswordDto());
            return "profile-user-password-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errors", e.getMessage());
            return "error404";
        }
    }

    @PostMapping("/update/password")
    public String updateUser(@Valid @ModelAttribute("dto") ChangePasswordDto dto,
                             BindingResult errors,
                             Model model,
                             HttpSession session) {
        User executor;
        try {
            executor = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "error401";
        }
        if (errors.hasErrors()) {
            return "profile-user-password-update";
        }

        if (!dto.getNewPassword().equals(dto.getNewPasswordConfirm())) {
            errors.rejectValue("newPasswordConfirm", "password_error", PASSWORDS_DO_NOT_MATCH);
            return "profile-user-password-update";
        }

        try {
            authenticationHelper.verifyAuthentication(executor.getUsername(), dto.getOldPassword());

            userService.updatePassword(executor, dto.getNewPassword());

            return "redirect:/myaccount";
        } catch (AuthenticationFailureException e) {
            errors.rejectValue("oldPassword", "invalid", INVALID_OLD_PASSWORD);
            return "profile-user-password-update";
        } catch (InvalidPasswordException e) {
            errors.rejectValue("newPassword", "invalid", e.getMessage());
            return "profile-user-password-update";
        } catch (UnauthorizedOperationException e) {
            return "error401";
        }
    }

}
