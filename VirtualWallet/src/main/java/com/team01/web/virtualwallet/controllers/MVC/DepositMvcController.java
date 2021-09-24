package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.exceptions.BadLuckException;
import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.DepositDto;
import com.team01.web.virtualwallet.services.contracts.CardService;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.TransferModelMapper;
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

@Controller
@RequestMapping("/myaccount/deposit")
public class DepositMvcController {

    private final UserService userService;
    private final TransferModelMapper transferModelMapper;
    private final TransferService transferService;
    private final CardService cardService;

    public DepositMvcController(UserService userService, TransferModelMapper transferModelMapper, TransferService transferService, CardService cardService) {
        this.userService = userService;
        this.transferModelMapper = transferModelMapper;
        this.transferService = transferService;
        this.cardService = cardService;
    }

    @GetMapping()
    public String showDeposit(Model model) {
        model.addAttribute("deposit", new DepositDto());
        return "deposit";
    }

    @ModelAttribute("user")
    public User populateUser(HttpSession session) {
        return userService.getByUsername(String.valueOf(session.getAttribute("currentUser")));
    }

    @ModelAttribute("cards")
    public Set<Card> populateCards(HttpSession session) {
        return populateUser(session).getCards();
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }


    @PostMapping()
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

}
