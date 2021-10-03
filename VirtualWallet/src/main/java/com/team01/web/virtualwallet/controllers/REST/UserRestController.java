package com.team01.web.virtualwallet.controllers.REST;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.controllers.GlobalExceptionHandler;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.*;
import com.team01.web.virtualwallet.models.enums.UserSortOptions;
import com.team01.web.virtualwallet.services.contracts.CardService;
import com.team01.web.virtualwallet.services.contracts.TransactionService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.CardModelMapper;
import com.team01.web.virtualwallet.services.utils.TransactionModelMapper;
import com.team01.web.virtualwallet.services.utils.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService service;
    private final UserModelMapper modelMapper;
    private final CardService cardService;
    private final TransactionService transactionService;
    private final TransactionModelMapper transactionModelMapper;
    private final CardModelMapper cardModelMapper;
    private final AuthenticationHelper authenticationHelper;
    private final GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    public UserRestController(UserService service,
                              UserModelMapper modelMapper,
                              CardService cardService, TransactionService transactionService, TransactionModelMapper transactionModelMapper, CardModelMapper cardModelMapper, AuthenticationHelper authenticationHelper,
                              GlobalExceptionHandler globalExceptionHandler) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.cardService = cardService;
        this.transactionService = transactionService;
        this.transactionModelMapper = transactionModelMapper;
        this.cardModelMapper = cardModelMapper;
        this.authenticationHelper = authenticationHelper;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @GetMapping
    public List<UserDto> getAll(@RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetAdmin(headers);
        return service.getAll().stream()
                .map(modelMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetAdmin(headers);
        return modelMapper.toDto(service.getById(id));
    }


    @GetMapping("/filter")
    public List<UserDto> filter(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String sortParam,
            @RequestHeader HttpHeaders headers) {

        authenticationHelper.tryGetAdmin(headers);

        var params = new FilterUserParams()
                .setUsername(username)
                .setEmail(email)
                .setPhoneNumber(phoneNumber)
                .setSortParam(UserSortOptions.valueOfPreview(sortParam));

        return service.filterUsers(params)
                .stream()
                .map(user -> modelMapper.toDto(user))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionDto> getUserTransfers(@PathVariable int id,
                                                 @RequestHeader HttpHeaders headers,
                                                 @RequestParam(required = false) String direction) {

        User executor = authenticationHelper.tryGetUser(headers);
        User user = service.getById(id);
        return transactionService.getUserTransactions(user, executor)
                .stream()
                .map(transfer -> transactionModelMapper.toDto(transfer))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/cards")
    public List<CardDto> getUserCards(@PathVariable int id,
                                      @RequestHeader HttpHeaders headers) {

        User executor = authenticationHelper.tryGetUser(headers);
        User user = service.getById(id);
        return cardService.getUserCards(user, executor)
                .stream()
                .map(card -> cardModelMapper.toDto(card))
                .collect(Collectors.toList());

    }

    @PostMapping
    public UserDto create(@Valid @RequestBody CreateUserDto dto, BindingResult result) {
        globalExceptionHandler.checkValidFields(result);
        User user = modelMapper.fromDto(dto);
        service.create(user);
        return modelMapper.toDto(user);
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable int id, @Valid @RequestBody UpdateUserDto dto, BindingResult result, @RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetUser(headers);
        globalExceptionHandler.checkValidFields(result);

        User user = modelMapper.fromDto(dto, id);
        service.update(user);
        return modelMapper.toDto(user);
    }

    @PutMapping("/block")
    public UserDto blockUser(@RequestBody BlockUserDto dto, @RequestHeader HttpHeaders headers) {
        User executor = authenticationHelper.tryGetAdmin(headers);
        User user = service.blockUserByAdmin(dto.getUsername(), executor);
        return modelMapper.toDto(user);
    }

    @PutMapping("/unblock")
    public UserDto unblock(@RequestBody BlockUserDto dto, @RequestHeader HttpHeaders headers) {
        User executor = authenticationHelper.tryGetAdmin(headers);
        User user = service.unblockUserByAdmin(dto.getUsername(), executor);
        return modelMapper.toDto(user);
    }

    @DeleteMapping("/{id}")
    public UserDto delete(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetAdmin(headers);
        User user = service.getById(id);
        UserDto dto = modelMapper.toDto(user);
        service.delete(id);
        return dto;
    }
}
