package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.Transaction;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

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

    @ModelAttribute("transactions")
    public List<Transaction> populateTransactions(HttpSession session){
        try {
            User user = userService.getByUsername(String.valueOf(session.getAttribute("currentUser")));
            //todo change with method - limit 5-10
            return userService.getUserLatestTransactions(user);
        }catch (EntityNotFoundException e){
            showHomePage(session);
            return List.of();
        }
    }

    @ModelAttribute("currentWalletId")
    public int populateWalletId(HttpSession session){
        try {
            User user = userService.getByUsername(String.valueOf(session.getAttribute("currentUser")));
            return user.getWallet().getId();
        }catch (EntityNotFoundException e){
            showHomePage(session);
            return 0;
        }
    }


}
