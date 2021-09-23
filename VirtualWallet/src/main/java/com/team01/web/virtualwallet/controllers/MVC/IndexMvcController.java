package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.TransactionDto;
import com.team01.web.virtualwallet.models.dto.TransferDto;
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
    private final TransferService transferService;
    private final TransferModelMapper transferModelMapper;
    private final TransactionModelMapper transactionModelMapper;

    @Autowired
    public IndexMvcController(UserService userService, TransferService transferService, TransferModelMapper transferModelMapper, TransactionModelMapper transactionModelMapper) {
        this.userService = userService;
        this.transferService = transferService;
        this.transferModelMapper = transferModelMapper;
        this.transactionModelMapper = transactionModelMapper;
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
    public List<TransactionDto> populateTransactions(HttpSession session){
        try {
            User user = userService.getByUsername(String.valueOf(session.getAttribute("currentUser")));
            return userService.getUserLatestTransactions(user)
                    .stream()
                    .map(transactionModelMapper::toDto)
                    .collect(Collectors.toList());
        }catch (EntityNotFoundException e){
            showHomePage(session);
            return List.of();
        }
    }


    @ModelAttribute("transfers")
    public List<TransferDto> populateTransfers(HttpSession session){
        try {
            User user = userService.getByUsername(String.valueOf(session.getAttribute("currentUser")));
            return transferService.getUserLatestTransfers(user).stream()
                    .map(transferModelMapper::toDto)
                    .collect(Collectors.toList());

        }catch (EntityNotFoundException e){
            showHomePage(session);
            return List.of();
        }
    }

}
