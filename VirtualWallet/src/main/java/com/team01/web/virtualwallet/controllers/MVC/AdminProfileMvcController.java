package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.exceptions.AuthenticationFailureException;
import com.team01.web.virtualwallet.exceptions.UnauthorizedOperationException;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.*;
import com.team01.web.virtualwallet.services.contracts.TransactionService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.TransactionModelMapper;
import com.team01.web.virtualwallet.services.utils.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/myaccount/admin")
public class AdminProfileMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final UserModelMapper userModelMapper;
    private final TransactionService transactionService;
    private final TransactionModelMapper transactionModelMapper;

    @Autowired
    public AdminProfileMvcController(AuthenticationHelper authenticationHelper, UserService userService, UserModelMapper userModelMapper, TransactionService transactionService, TransactionModelMapper transactionModelMapper) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.userModelMapper = userModelMapper;
        this.transactionService = transactionService;
        this.transactionModelMapper = transactionModelMapper;
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

    @ModelAttribute("usersDto")
    public List<UserDto> populateUsersDto() {
        return userService.getAll()
                .stream()
                .map(user -> userModelMapper.toDto(user))
                .collect(Collectors.toList());
    }

    @ModelAttribute("users")
    public List<User> populateUsers() {
        return userService.getAll();
    }

    @ModelAttribute("transactions")
    public List<TransactionDto> populateTransactions() {
        return transactionService.getAll()
                .stream()
                .map(transaction -> transactionModelMapper.toDto(transaction))
                .collect(Collectors.toList());
    }


    @GetMapping("")
    public String showAdminPage(HttpSession session, Model model) {
        try {
            authenticationHelper.tryGetAdmin(session);

            model.addAttribute("searchUser", new SearchUserMvcDto());
            model.addAttribute("blockDto", new BlockUserDto());
            model.addAttribute("filterTransaction", new AdminFilterTransactionMvcDto());
            return "admin-menu";
        } catch (UnauthorizedOperationException | AuthenticationFailureException e) {
            model.addAttribute("error", e.getMessage());
            return "error401";
        }

    }

    @GetMapping("/users/search")
    public String searchUsers(
            @ModelAttribute("searchUser") SearchUserMvcDto dto,
            HttpSession session,
            Model model) {
        try {
            authenticationHelper.tryGetAdmin(session);

            var params = new FilterUserParams()
                    .setPhoneNumber(dto.getPhoneNumber())
                    .setUsername(dto.getUsername())
                    .setEmail(dto.getEmail());

            var filtered = userService.filterUsers(params)
                    .stream()
                    .map(user -> userModelMapper.toDto(user))
                    .collect(Collectors.toList());
            model.addAttribute("usersDto", filtered);
            model.addAttribute("filterTransaction", new AdminFilterTransactionMvcDto());

            return "admin-menu";
        } catch (AuthenticationFailureException | UnauthorizedOperationException e) {
            return "error401";
        }
    }


    @PostMapping("/block")
    public String blockUser(
            @ModelAttribute("blockDto") BlockUserDto dto,
            HttpSession session,
            Model model) {
        try {
            User user = authenticationHelper.tryGetAdmin(session);
            userService.blockUserByAdmin(dto.getUsername(), user);
            model.addAttribute("searchUser", new SearchUserMvcDto());
            model.addAttribute("filterTransaction", new AdminFilterTransactionMvcDto());

            return "redirect:/myaccount/admin";
        } catch (AuthenticationFailureException | UnauthorizedOperationException e) {
            return "error401";
        }
    }


    @PostMapping("/unblock")
    public String unblockUser(
            @ModelAttribute("blockDto") BlockUserDto dto,
            HttpSession session,
            Model model) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            userService.unblockUserByAdmin(dto.getUsername(), user);
            model.addAttribute("search", new SearchUserMvcDto());
            model.addAttribute("filterTransaction", new AdminFilterTransactionMvcDto());

            return "redirect:/myaccount/admin";
        } catch (AuthenticationFailureException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/transactions/search")
    public String searchTransactions(
            @ModelAttribute("filterTransaction") AdminFilterTransactionMvcDto dto,
            HttpSession session,
            Model model) {

        Optional<LocalDateTime> startDate = dto.getStartDate().isEmpty() ? Optional.empty() : Optional.of(stringToLocalDate(dto.getStartDate()));
        Optional<LocalDateTime> endDate = dto.getEndDate().isEmpty() ? Optional.empty() : Optional.of(stringToLocalDate(dto.getEndDate()));
        Optional<Integer> senderId = dto.getSenderId() != -1 ? Optional.of(dto.getSenderId()) : Optional.empty();
        Optional<Integer> receiverId = dto.getReceiverId() != -1 ? Optional.of(dto.getReceiverId()) : Optional.empty();


        var params = new FilterTransactionByAdminParams()
                .setEndDate(endDate)
                .setStartDate(startDate)
                .setReceiverId(receiverId)
                .setSenderId(senderId)
                .setSortParam("");

        var filtered = transactionService.adminFilterTransactions(params)
                .stream()
                .map(transactions -> transactionModelMapper.toDto(transactions))
                .collect(Collectors.toList());

        model.addAttribute("transactions", filtered);
        model.addAttribute("searchUser", new SearchUserMvcDto());

        return "admin-menu";
    }

    private LocalDateTime stringToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return LocalDateTime.parse(date, formatter);
    }

}
