package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.FilterTransactionDto;
import com.team01.web.virtualwallet.services.contracts.TransactionService;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.TransactionModelMapper;
import com.team01.web.virtualwallet.services.utils.TransferModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/myaccount")
public class UserProfileMvcController {

    private final UserService userService;
    private final TransferService transferService;
    private final TransferModelMapper transferModelMapper;
    private final TransactionModelMapper transactionModelMapper;
    private final TransactionService transactionService;

    @Autowired
    public UserProfileMvcController(UserService userService, TransferService transferService, TransferModelMapper transferModelMapper, TransactionModelMapper transactionModelMapper, TransactionService transactionService) {
        this.userService = userService;
        this.transferService = transferService;
        this.transferModelMapper = transferModelMapper;
        this.transactionModelMapper = transactionModelMapper;
        this.transactionService = transactionService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("user")
    public User populateUser(HttpSession session) {
        return userService.getByUsername(String.valueOf(session.getAttribute("currentUser")));
    }

    @ModelAttribute("filterDto")
    public FilterTransactionDto populateFilterDto() {
        return new FilterTransactionDto();
    }

    @GetMapping
    public String showUserProfile() {
        return "profile-user";
    }

}
