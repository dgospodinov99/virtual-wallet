package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.exceptions.*;
import com.team01.web.virtualwallet.models.Token;
import com.team01.web.virtualwallet.models.Transaction;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.*;
import com.team01.web.virtualwallet.models.enums.TransactionDirection;
import com.team01.web.virtualwallet.models.enums.UserSortOptions;
import com.team01.web.virtualwallet.services.contracts.EmailService;
import com.team01.web.virtualwallet.services.contracts.TokenService;
import com.team01.web.virtualwallet.services.contracts.TransactionService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.Helpers;
import com.team01.web.virtualwallet.services.utils.TransactionModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("myaccount/transactions")
public class TransactionMvcController {

    private final UserService userService;
    private final TransactionService transactionService;
    private final TransactionModelMapper transactionModelMapper;
    private final AuthenticationHelper authenticationHelper;
    private final EmailService emailService;
    private final TokenService tokenService;

    @Autowired
    public TransactionMvcController(UserService userService,
                                    TransactionService transactionService,
                                    TransactionModelMapper transactionModelMapper,
                                    AuthenticationHelper authenticationHelper, EmailService emailService,
                                    TokenService tokenService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.transactionModelMapper = transactionModelMapper;
        this.authenticationHelper = authenticationHelper;
        this.emailService = emailService;
        this.tokenService = tokenService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        try {
            authenticationHelper.tryGetAdmin(session);
            return true;
        } catch (AuthenticationFailureException | UnauthorizedOperationException e) {
            return false;
        }
    }

    @ModelAttribute("users")
    public List<User> populateUsersList(HttpSession session) {

        var users = userService.getAll();
        users.remove(authenticationHelper.tryGetUser(session));
        return users;


    }

    @ModelAttribute("filterDto")
    public FilterTransactionMvcDto populateFilterDto() {
        return new FilterTransactionMvcDto();
    }

    @GetMapping("")
    public String showActivity(HttpSession session, Model model) {

        User user = authenticationHelper.tryGetUser(session);

        var transactions = transactionService.getUserTransactions(user, user)
                .stream()
                .map(transaction -> transactionModelMapper.toDto(transaction))
                .collect(Collectors.toList());

        model.addAttribute("transactions", transactions);
        return "transactions";


    }

    @GetMapping("/filter")
    public String filterTransactions(@ModelAttribute("filterDto") FilterTransactionMvcDto dto, HttpSession session, Model model) {

        User user = authenticationHelper.tryGetUser(session);

        dto.setWalletId(user.getWallet().getId());
        Optional<TransactionDirection> direction = dto.getDirection() == null ? Optional.empty() : Optional.of(dto.getDirection());
        Optional<LocalDateTime> startDate = dto.getStartDate().isEmpty() ? Optional.empty() : Optional.of(Helpers.stringToLocalDate(dto.getStartDate()));
        Optional<LocalDateTime> endDate = dto.getEndDate().isEmpty() ? Optional.empty() : Optional.of(Helpers.stringToLocalDate(dto.getEndDate()));

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

    @GetMapping("/new")
    public String showTransaction(Model model, HttpSession session) {

        authenticationHelper.tryGetUser(session);
        model.addAttribute("transaction", new CreateTransactionDto());
        model.addAttribute("searchUser", new SearchUserMvcDto());
        return "transaction-new";


    }

    @GetMapping("/users/search")
    public String searchUsers(
            @ModelAttribute("searchUser") SearchUserMvcDto dto,
            HttpSession session,
            Model model) {


        User executor = authenticationHelper.tryGetUser(session);
        var params = new FilterUserParams()
                .setPhoneNumber(dto.getPhoneNumber())
                .setUsername(dto.getUsername())
                .setEmail(dto.getEmail())
                .setSortParam(UserSortOptions.valueOfPreview(String.valueOf(dto.getSortParam())));

        var filtered = userService.filterUsers(params);
        filtered.remove(executor);
        model.addAttribute("usersDto", filtered);

        return "transaction-new";

    }

    @GetMapping("/new/finalize")
    public String showTransactionFinalize(@RequestParam Integer receiverId, Model model, HttpSession session) {

        authenticationHelper.tryGetUser(session);
        CreateTransactionDto dto = new CreateTransactionDto();
        dto.setReceiverId(receiverId);
        model.addAttribute("transaction", dto);
        model.addAttribute("receiverUsername", userService.getById(receiverId).getUsername());
        return "transaction-finalize";


    }


    @PostMapping("/new/finalize")
    public String createTransaction(@Valid @ModelAttribute("transaction") CreateTransactionDto dto, BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            return "transaction-finalize";
        }
        try {
            User user = authenticationHelper.tryGetUser(session);
            Transaction transaction = transactionModelMapper.fromDto(dto);
            transaction.setSender(user.getWallet());
            transactionService.checkForLargeTransaction(transaction);

            transactionService.create(transaction, user);
            return "redirect:/myaccount/transactions";
        } catch (InvalidTransferException e) {
            model.addAttribute("receiverUsername", userService.getById(dto.getReceiverId()).getUsername());
            bindingResult.rejectValue("amount", "amount_error", e.getMessage());
            return "transaction-finalize";
        } catch (InvalidUserInput | BlockedUserException | UnauthorizedOperationException e) {
            bindingResult.rejectValue("receiverId", "transaction_error", e.getMessage());
            return "transaction-finalize";
        } catch (LargeTransactionDetectedException e) {
            session.setAttribute("createDto", dto);
            session.setAttribute("amount", dto.getAmount());
            session.setAttribute("receiverId", dto.getReceiverId());
            session.setAttribute("receiverUsername", userService.getById(dto.getReceiverId()).getUsername());
            emailService.sendVerifyTransactionEmail(authenticationHelper.tryGetUser(session).getEmail());
            return "redirect:/myaccount/transactions/verify";
        }
    }

    @GetMapping("/verify")
    public String showTransactionVerification(Model model,
                                              HttpSession session) {
        authenticationHelper.tryGetUser(session);
        User receiver = userService.getById((Integer) session.getAttribute("receiverId"));
        var largeDTO = transactionModelMapper.toLargeDto((CreateTransactionDto) session.getAttribute("createDto"));
        model.addAttribute("transactionDto", largeDTO);
        model.addAttribute("receiverUsername", receiver.getUsername());
        return "transaction-verify";
    }

    @PostMapping("/new/finalize/verify")
    public String handleLargeTransaction(@ModelAttribute("transactionDto") LargeTransactionDto dto,
                                         BindingResult bindingResult,
                                         HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "transaction-verify";
        }
        try {
            dto.setAmount((Double) session.getAttribute("amount"));
            dto.setReceiverId((Integer) session.getAttribute("receiverId"));
            Transaction transaction = transactionModelMapper.fromLargeDto(dto);
            User sender = authenticationHelper.tryGetUser(session);
            transaction.setSender(sender.getWallet());
            Token toValidate = tokenService.getByToken(dto.getToken());
            tokenService.validateCorrectToken(toValidate, sender);
            transactionService.create(transaction, sender);
            tokenService.delete(toValidate.getId());
            return "redirect:/myaccount/transactions";
        } catch (InvalidTokenException | EntityNotFoundException e) {
            bindingResult.rejectValue("token", "token_error", e.getMessage());
            return "transaction-verify";
        }
    }
}
