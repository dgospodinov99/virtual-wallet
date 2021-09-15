package com.team01.web.virtualwallet.controllers.REST;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.controllers.GlobalExceptionHandler;
import com.team01.web.virtualwallet.exceptions.*;
import com.team01.web.virtualwallet.models.Transaction;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.*;
import com.team01.web.virtualwallet.services.contracts.TransactionService;
import com.team01.web.virtualwallet.services.utils.TransactionModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public List<TransactionDto> getAll() {
        return transactionService.getAll().stream()
                .map(transaction -> modelMapper.toDto(transaction))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TransactionDto getById(@PathVariable int id) {
        try {
            return modelMapper.toDto(transactionService.getById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/filter")
    public List<TransactionDto> filter(
            @RequestParam(required = false) Integer senderId,
            @RequestParam(required = false) Integer receiverId,
            @RequestParam(required = false) String sortParam,
            @RequestParam(required = false) String direction,
            @RequestHeader HttpHeaders headers) {
        try {
            var params = new FilterTransactionParams()
                    .setSenderUsername(senderId)
                    .setReceiverUsername(receiverId)
                    .setSortParam(sortParam)
                    .setDirection(direction);

            return transactionService.filterTransactions(params)
                    .stream()
                    .map(transaction -> modelMapper.toDto(transaction))
                    .collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public TransactionDto create(@Valid @RequestBody CreateTransactionDto dto, @RequestHeader HttpHeaders headers, BindingResult result) {
        globalExceptionHandler.checkValidFields(result);
        try {
            User executor = authenticationHelper.tryGetUser(headers);

            Transaction transaction = modelMapper.fromDto(dto);
            transactionService.create(transaction, executor);
            return modelMapper.toDto(transaction);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidTransferException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (DuplicateEntityException | InvalidPasswordException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
