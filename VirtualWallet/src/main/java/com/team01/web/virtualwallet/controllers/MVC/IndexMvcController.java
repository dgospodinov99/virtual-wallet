package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.exceptions.UnauthorizedOperationException;
import com.team01.web.virtualwallet.models.Transaction;
import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.TransactionDto;
import com.team01.web.virtualwallet.models.dto.TransferDto;
import com.team01.web.virtualwallet.services.contracts.TransactionService;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import com.team01.web.virtualwallet.services.utils.TransactionModelMapper;
import com.team01.web.virtualwallet.services.utils.TransferModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class IndexMvcController {

    private final TransactionService transactionService;
    private final TransferService transferService;
    private final TransferModelMapper transferModelMapper;
    private final TransactionModelMapper transactionModelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public IndexMvcController(TransactionService transactionService, TransferService transferService, TransferModelMapper transferModelMapper, TransactionModelMapper transactionModelMapper, AuthenticationHelper authenticationHelper) {
        this.transactionService = transactionService;
        this.transferService = transferService;
        this.transferModelMapper = transferModelMapper;
        this.transactionModelMapper = transactionModelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showHomePage(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            model.addAttribute("transfers", populateTransfers(session));
            model.addAttribute("transactions", populateTransactions(session));
            model.addAttribute("balance", populateBalance(session));
            model.addAttribute("isAdmin", populateIsAdmin(session));
            model.addAttribute("earnings", populateEarnings(session));
            return "index";
        } catch (ResponseStatusException e) {
            return "redirect:/auth/login";
        }

    }

    public boolean populateIsAdmin(HttpSession session) {
        try {
            authenticationHelper.tryGetAdmin(session);
            return true;
        } catch (UnauthorizedOperationException e) {
            return false;
        }
    }

    public double populateBalance(HttpSession session) {

        User user = authenticationHelper.tryGetUser(session);
        return user.getWallet().getBalance();


    }

    public Map<String, Double> populateEarnings(HttpSession session) {
        User user = authenticationHelper.tryGetUser(session);
        User mockUser = new User();

        List<Transaction> earnings = transactionService.getUserTransactions(user, user);
        List<Transfer> transfers = transferService.getUserLatestTransfers(user);
        double income = 0;
        double outcome = 0;
        double deposit = 0;
        Map<String, Double> data = new LinkedHashMap<>();

        if (!earnings.isEmpty()) {
            for (Transaction transaction : earnings) {
                if (transaction.getSender().getId() == user.getId()) {
                    outcome += transaction.getAmount();
                } else {
                    income += transaction.getAmount();
                }
            }   
            data.put("Income ", income);
            data.put("Outcome ", outcome);
        }

        if (!transfers.isEmpty()) {
            for (Transfer transfer : transfers) {
                deposit += transfer.getAmount();
            }
            data.put("Deposits", deposit);
        }

        return data;
    }
    public List<TransactionDto> populateTransactions(HttpSession session) {

        User user = authenticationHelper.tryGetUser(session);
        return transactionService.getUserLatestTransactions(user)
                .stream()
                .map(transactionModelMapper::toDto)
                .collect(Collectors.toList());

    }

    public List<TransferDto> populateTransfers(HttpSession session) {

        User user = authenticationHelper.tryGetUser(session);
        return transferService.getUserLatestTransfers(user).stream()
                .map(transferModelMapper::toDto)
                .collect(Collectors.toList());


    }
}
