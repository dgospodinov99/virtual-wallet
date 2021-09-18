package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Set;

@Controller
@RequestMapping("/myaccount")
public class UserProfileMvcController {

    private final UserService userService;

    @Autowired
    public UserProfileMvcController(UserService userService){
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("cards")
    public Set<Card> populateUserCards(HttpSession session) {
        return populateUser(session).getCards();
    }

    @ModelAttribute("user")
    public User populateUser(HttpSession session) {
        return userService.getByUsername(String.valueOf(session.getAttribute("currentUser")));
    }

    @GetMapping
    public String showUserProfile(){
        return "profile-user";
    }

    @GetMapping("/cards")
    public String showDeposit(){
        return "cards";
    }
}
