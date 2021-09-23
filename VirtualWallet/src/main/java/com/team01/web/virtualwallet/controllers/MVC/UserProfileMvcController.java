package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.exceptions.BadLuckException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.DepositDto;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.TransactionModelMapper;
import com.team01.web.virtualwallet.services.utils.TransferModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/myaccount")
public class UserProfileMvcController {

    private final UserService userService;
    private final TransferService transferService;
    private final TransferModelMapper transferModelMapper;
    private final TransactionModelMapper transactionModelMapper;

    @Autowired
    public UserProfileMvcController(UserService userService, TransferService transferService, TransferModelMapper transferModelMapper, TransactionModelMapper transactionModelMapper) {
        this.userService = userService;
        this.transferService = transferService;
        this.transferModelMapper = transferModelMapper;
        this.transactionModelMapper = transactionModelMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("user")
    public User populateUser(HttpSession session) {
        return userService.getByUsername(String.valueOf(session.getAttribute("currentUser")));
    }

    @ModelAttribute("cards")
    public Set<Card> populateUserCards(HttpSession session) {
        try {
            return populateUser(session).getCards();
        } catch (EntityNotFoundException e){
            return null;
        }
    }

    @ModelAttribute("currentWalletId")
    public int populateWalletId(HttpSession session){
        User user = userService.getByUsername(String.valueOf(session.getAttribute("currentUser")));
        return user.getWallet().getId();
    }

    @GetMapping
    public String showUserProfile() {
        return "profile-user";
    }

    @GetMapping("/cards")
    public String showCards(HttpSession session) {
        try {
            if(session.getAttribute("currentUser") == null){
                return "redirect:/auth/login";
            }
            return "cards";
        }catch (EntityNotFoundException e){
            return "redirect:/auth/login";
        }

    }

    @GetMapping("/deposit")
    public String showDeposit(Model model) {
        model.addAttribute("deposit", new DepositDto());
        return "deposit";
    }

    @PostMapping("/deposit")
    public String handleDeposit(@Valid @ModelAttribute("deposit") DepositDto dto,
                                BindingResult bindingResult,
                                HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "deposit";
        }

        try {
            User user = userService.getByUsername(String.valueOf(session.getAttribute("currentUser")));
            Transfer transfer = transferModelMapper.fromDto(dto, user.getWallet());
            transferService.create(transfer);
            return "redirect:/";
        } catch (BadLuckException | IOException e) {
            bindingResult.rejectValue("amount", "auth_error", e.getMessage());
            return "deposit";
        }
    }

    @GetMapping("/transactions")
    public String showActivity(HttpSession session, Model model) {
        User user = userService.getByUsername(String.valueOf(session.getAttribute("currentUser")));

        var transactions = userService.getUserTransactions(user.getId(),user)
                .stream()
                .map(transaction -> transactionModelMapper.toDto(transaction))
                .collect(Collectors.toList());

        model.addAttribute("transactions", transactions);
        return "transactions";
    }



}
