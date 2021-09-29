package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.exceptions.AuthenticationFailureException;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.BlockUserDto;
import com.team01.web.virtualwallet.models.dto.FilterUserParams;
import com.team01.web.virtualwallet.models.dto.SearchUserMvcDto;
import com.team01.web.virtualwallet.models.dto.UserDto;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/myaccount/admin")
public class AdminProfileMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final UserModelMapper userModelMapper;

    @Autowired
    public AdminProfileMvcController(AuthenticationHelper authenticationHelper, UserService userService, UserModelMapper userModelMapper) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.userModelMapper = userModelMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        try {
            return authenticationHelper.tryGetUser(session).isAdmin();
        } catch (AuthenticationFailureException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ModelAttribute("users")
    public List<UserDto> populateUsers(HttpSession session) {
        return userService.getAll()
                .stream()
                .map(user -> userModelMapper.toDto(user))
                .collect(Collectors.toList());
    }


    @GetMapping("")
    public String showAdminPage(HttpSession session, Model model) {
        User user = authenticationHelper.tryGetUser(session);

        if (!user.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Must be admin to view this!");
        }
        model.addAttribute("search", new SearchUserMvcDto());
        model.addAttribute("blockDto", new BlockUserDto());
        return "admin-menu";
    }

    @GetMapping("/search")
    public String searchUsers(
            @ModelAttribute("search") SearchUserMvcDto dto,
            HttpSession session,
            Model model) {

        var params = new FilterUserParams()
                .setPhoneNumber(dto.getPhoneNumber())
                .setUsername(dto.getUsername())
                .setEmail(dto.getEmail());

        var filtered = userService.filterUsers(params)
                .stream()
                .map(user -> userModelMapper.toDto(user))
                .collect(Collectors.toList());
        model.addAttribute("users", filtered);

        return "admin-menu";
    }


    @PostMapping("/block")
    public String blockUser(
            @ModelAttribute("blockDto") BlockUserDto dto,
            HttpSession session,
            Model model) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            userService.blockUser(dto.getUsername(), user);
            model.addAttribute("search", new SearchUserMvcDto());

            return "redirect:/myaccount/admin";
        } catch (AuthenticationFailureException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @PostMapping("/unblock")
    public String unblockUser(
            @ModelAttribute("blockDto") BlockUserDto dto,
            HttpSession session,
            Model model) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            userService.unblockUser(dto.getUsername(), user);
            model.addAttribute("search", new SearchUserMvcDto());

            return "redirect:/myaccount/admin";
        } catch (AuthenticationFailureException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
