package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.exceptions.BadLuckException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.DepositDto;
import com.team01.web.virtualwallet.models.dto.FilterTransactionDto;
import com.team01.web.virtualwallet.models.dto.FilterTransactionsByUserParams;
import com.team01.web.virtualwallet.models.enums.TransactionDirection;
import com.team01.web.virtualwallet.services.contracts.TransactionService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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



    @GetMapping("/transactions")
    public String showActivity(HttpSession session, Model model) {
        User user = userService.getByUsername(String.valueOf(session.getAttribute("currentUser")));

        var transactions = userService.getUserTransactions(user.getId(), user)
                .stream()
                .map(transaction -> transactionModelMapper.toDto(transaction))
                .collect(Collectors.toList());

        model.addAttribute("transactions", transactions);
        return "transactions";
    }

    @GetMapping("/transactions/filter")
    public String filterTransactions(@ModelAttribute FilterTransactionDto dto, HttpSession session, Model model) {
        dto.setWalletId(populateUser(session).getWallet().getId());
        Optional<TransactionDirection> direction = dto.getDirection() == null ? Optional.empty() : Optional.of(dto.getDirection());
        Optional<LocalDateTime> startDate = dto.getStartDate().isEmpty() ? Optional.empty() : Optional.of(stringToLocalDate(dto.getStartDate()));
        Optional<LocalDateTime> endDate = dto.getEndDate().isEmpty() ? Optional.empty() : Optional.of(stringToLocalDate(dto.getEndDate()));


        var params = new FilterTransactionsByUserParams()
                .setEndDate(endDate)
                .setStartDate(startDate)
                .setDirection(direction)
                .setWalletId(Optional.of(dto.getWalletId()));

        var filtered = transactionService.userFilterTransactions(params)
                .stream()
                .map(transaction -> transactionModelMapper.toDto(transaction))
                .collect(Collectors.toList());;

        model.addAttribute("transactions",filtered);
        return "transactions";
    }

    private LocalDateTime stringToLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return LocalDateTime.parse(date,formatter);
    }

}
