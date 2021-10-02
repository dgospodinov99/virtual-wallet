package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.exceptions.AuthenticationFailureException;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.TransactionDto;
import com.team01.web.virtualwallet.models.dto.TransferDto;
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
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class IndexMvcController {

    private final UserService userService;
    private final TransactionService transactionService;
    private final TransferService transferService;
    private final TransferModelMapper transferModelMapper;
    private final TransactionModelMapper transactionModelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public IndexMvcController(UserService userService, TransactionService transactionService, TransferService transferService, TransferModelMapper transferModelMapper, TransactionModelMapper transactionModelMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.transferService = transferService;
        this.transferModelMapper = transferModelMapper;
        this.transactionModelMapper = transactionModelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public String showHomePage(HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            return "index";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

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
            showHomePage(session);
            return false;
        }
    }


    @ModelAttribute("balance")
    public double populateBalance(HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            return user.getWallet().getBalance();
        } catch (AuthenticationFailureException e) {
            showHomePage(session);
            return 0;
        }
    }

    @ModelAttribute("transactions")
    public List<TransactionDto> populateTransactions(HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            return transactionService.getUserLatestTransactions(user)
                    .stream()
                    .map(transactionModelMapper::toDto)
                    .collect(Collectors.toList());
        } catch (AuthenticationFailureException e) {
            showHomePage(session);
            return List.of();
        }
    }


    @ModelAttribute("transfers")
    public List<TransferDto> populateTransfers(HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            return transferService.getUserLatestTransfers(user).stream()
                    .map(transferModelMapper::toDto)
                    .collect(Collectors.toList());

        } catch (AuthenticationFailureException e) {
            showHomePage(session);
            return List.of();
        }
    }

}
