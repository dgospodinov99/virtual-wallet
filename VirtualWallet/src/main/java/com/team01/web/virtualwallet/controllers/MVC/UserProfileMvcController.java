package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.exceptions.*;
import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.CreateCardDto;
import com.team01.web.virtualwallet.models.dto.UpdateUserDto;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.format.DateTimeParseException;

@Controller
@RequestMapping("/myaccount")
public class UserProfileMvcController {

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

    @ModelAttribute("user")
    public User populateUser(HttpSession session) {
        return userService.getByUsername(String.valueOf(session.getAttribute("currentUser")));
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
            return "redirect:/unauthorized";
        }

        try {
            User user = populateUser(session);
            UpdateUserDto dto = modelMapper.toUpdateDto(user);
            model.addAttribute("dto", dto);
            return "profile-user-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errors", e.getMessage());
            return "404";
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
            return "redirect:/unauthorized";
        }
        if (errors.hasErrors()) {
            return "profile-user-update";
        }

        try {
            User user = modelMapper.fromDto(dto, populateUser(session).getId());
            userService.update(user);

            return "redirect:/myaccount";
        } catch (UnauthorizedOperationException e) {
            return "redirect:/unauthorized";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("email", "duplicate", e.getMessage());
            return "profile-user-update";
        }
    }

}
