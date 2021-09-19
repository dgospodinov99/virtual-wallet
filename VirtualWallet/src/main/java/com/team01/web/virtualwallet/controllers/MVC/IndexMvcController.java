package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class IndexMvcController {

    private final UserService userService;

    @Autowired
    public IndexMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showHomePage(HttpSession session) {
        try {
            if(session.getAttribute("currentUser") == null){
                return "redirect:/auth/login";
            }
            return "index";
        }catch (EntityNotFoundException e){
            return "redirect:/auth/login";
        }

    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("balance")
    public double populateBalance(HttpSession session){
        try {
            User user = userService.getByUsername(String.valueOf(session.getAttribute("currentUser")));
            return user.getWallet().getBalance();
        }catch (EntityNotFoundException e){
            showHomePage(session);
            return 0;
        }
    }


}
