package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/profile")
public class ProfileMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public ProfileMvcController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public String redirectToProfile(HttpSession session){
        if(isAdmin(session)){
            return "redirect:/admin";
        }else {
            return "redirect:/myaccount";
        }
    }

    private boolean isAdmin(HttpSession session){
        User user = authenticationHelper.tryGetUser(session);
        return user.isAdmin();
    }
}
