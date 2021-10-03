package com.team01.web.virtualwallet.controllers.REST;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.controllers.GlobalExceptionHandler;
import com.team01.web.virtualwallet.models.Transaction;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.CreateTransactionDto;
import com.team01.web.virtualwallet.models.dto.FilterTransactionByAdminParams;
import com.team01.web.virtualwallet.models.dto.TransactionDto;
import com.team01.web.virtualwallet.services.contracts.TransactionService;
import com.team01.web.virtualwallet.services.utils.Helpers;
import com.team01.web.virtualwallet.services.utils.TransactionModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionRestController {

    private final TransactionService transactionService;
    private final TransactionModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;
    private final GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    public TransactionRestController(TransactionService transactionService,
                                     TransactionModelMapper modelMapper,
                                     AuthenticationHelper authenticationHelper,
                                     GlobalExceptionHandler globalExceptionHandler) {
        this.transactionService = transactionService;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
        this.globalExceptionHandler = globalExceptionHandler;
    }


    @GetMapping()
    public List<TransactionDto> getAll(@RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetAdmin(headers);
        return transactionService.getAll().stream()
                .map(transaction -> modelMapper.toDto(transaction))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TransactionDto getById(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetAdmin(headers);
            return modelMapper.toDto(transactionService.getById(id));
    }

    @GetMapping("/filter")
    public List<TransactionDto> filter(
            @RequestParam(required = false) Integer senderId,
            @RequestParam(required = false) Integer receiverId,
            @RequestParam(required = false) String sortParam,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestHeader HttpHeaders headers) {

        authenticationHelper.tryGetAdmin(headers);

        var params = new FilterTransactionByAdminParams()
                .setSenderId(senderId)
                .setReceiverId(receiverId)
                .setStartDate(Helpers.stringToLocalDateTimeOptional(from))
                .setEndDate(Helpers.stringToLocalDateTimeOptional(to))
                .setSortParam(sortParam);

        return transactionService.adminFilterTransactions(params)
                .stream()
                .map(transaction -> modelMapper.toDto(transaction))
                .collect(Collectors.toList());
    }

    @PostMapping
    public TransactionDto create(@Valid @RequestBody CreateTransactionDto dto, @RequestHeader HttpHeaders headers, BindingResult result) {
        globalExceptionHandler.checkValidFields(result);

        User executor = authenticationHelper.tryGetUser(headers);
        Transaction transaction = modelMapper.fromDto(dto);
        transaction.setSender(executor.getWallet());
        transactionService.create(transaction, executor);
        return modelMapper.toDto(transaction);
    }


}
