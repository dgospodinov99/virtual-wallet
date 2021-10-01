package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.exceptions.*;
import com.team01.web.virtualwallet.models.Transaction;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.*;
import com.team01.web.virtualwallet.models.enums.TransactionDirection;
import com.team01.web.virtualwallet.services.contracts.TransactionService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.TransactionModelMapper;
import com.team01.web.virtualwallet.services.utils.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("myaccount/transactions")
public class TransactionMvcController {

    private final UserService userService;
    private final UserModelMapper userModelMapper;
    private final TransactionService transactionService;
    private final TransactionModelMapper transactionModelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public TransactionMvcController(UserService userService,
                                    UserModelMapper userModelMapper, TransactionService transactionService,
                                    TransactionModelMapper transactionModelMapper,
                                    AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.userModelMapper = userModelMapper;
        this.transactionService = transactionService;
        this.transactionModelMapper = transactionModelMapper;
        this.authenticationHelper = authenticationHelper;
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @ModelAttribute("user")
    public User populateUser(HttpSession session) {
        try {
            return authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ModelAttribute("users")
    public List<User> populateUsersList() {
        return userService.getAll();
    }

    @ModelAttribute("filterDto")
    public FilterTransactionMvcDto populateFilterDto() {
        return new FilterTransactionMvcDto();
    }

    @GetMapping("")
    public String showActivity(HttpSession session, Model model) {
        User user = authenticationHelper.tryGetUser(session);

        var transactions = userService.getUserTransactions(user.getId(), user)
                .stream()
                .map(transaction -> transactionModelMapper.toDto(transaction))
                .collect(Collectors.toList());

        model.addAttribute("transactions", transactions);
        return "transactions";
    }

    @GetMapping("/filter")
    public String filterTransactions(@ModelAttribute("filterDto") FilterTransactionMvcDto dto, HttpSession session, Model model) {
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
                .collect(Collectors.toList());


        model.addAttribute("transactions", filtered);
        return "transactions";
    }

    private LocalDateTime stringToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return LocalDateTime.parse(date, formatter);
    }

    @GetMapping("/new")
    public String showTransaction(Model model) {
        model.addAttribute("transaction", new CreateTransactionDto());
        model.addAttribute("searchUser", new SearchUserMvcDto());
        return "transaction-receiver";
    }

    @GetMapping("/users/search")
    public String searchUsers(
            @ModelAttribute("searchUser") SearchUserMvcDto dto,
            HttpSession session,
            Model model) {
        //todo duplicate
        var params = new FilterUserParams()
                .setPhoneNumber(dto.getPhoneNumber())
                .setUsername(dto.getUsername())
                .setEmail(dto.getEmail());

        var filtered = userService.filterUsers(params);
        model.addAttribute("usersDto", filtered);

        return "transaction-receiver";
    }

    @GetMapping("/new/finalize")
    public String showTransactionFinalize(@RequestParam Integer receiverId, Model model) {
        CreateTransactionDto dto = new CreateTransactionDto();
        dto.setReceiverId(receiverId);
        model.addAttribute("transaction", dto);
        model.addAttribute("receiverUsername", userService.getById(receiverId).getUsername());
        return "transaction-new";
    }

    @PostMapping("/new/finalize")
    public String createTransaction(@Valid @ModelAttribute("transaction") CreateTransactionDto dto, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "transaction-new";
        }
        try {
            User user = authenticationHelper.tryGetUser(session);
            Transaction transaction = transactionModelMapper.fromDto(dto);
            transaction.setSender(user.getWallet());
            transactionService.create(transaction, user);
            return "redirect:/myaccount/transactions";
        } catch (InvalidTransferException e) {
            bindingResult.rejectValue("amount", "amount_error", e.getMessage());
            return "transaction-new";
        } catch (InvalidUserInput | BlockedUserException | UnauthorizedOperationException e) {
            bindingResult.rejectValue("receiverId", "transaction_error", e.getMessage());
            return "transaction-new";
        }
    }

}
