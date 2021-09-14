package com.team01.web.virtualwallet.controllers.REST;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.controllers.GlobalExceptionHandler;
import com.team01.web.virtualwallet.exceptions.*;
import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.TransferDto;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import com.team01.web.virtualwallet.services.utils.TransferModelMapper;
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
@RequestMapping("/api/transfers")
public class TransferRestController {

    private final TransferService transferService;
    private final TransferModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;
    private final GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    public TransferRestController(TransferService transferService,
                                  TransferModelMapper modelMapper,
                                  AuthenticationHelper authenticationHelper,
                                  GlobalExceptionHandler globalExceptionHandler) {
        this.transferService = transferService;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
        this.globalExceptionHandler = globalExceptionHandler;
    }


    @GetMapping()
    public List<TransferDto> getAll() {
        return transferService.getAll().stream()
                .map(transfer -> modelMapper.toDto(transfer))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TransferDto getById(@PathVariable int id) {
        try {
            return modelMapper.toDto(transferService.getById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public TransferDto create(@Valid @RequestBody TransferDto dto, @RequestHeader HttpHeaders headers, BindingResult result) {
        globalExceptionHandler.checkValidFields(result);
        try {
            User executor = authenticationHelper.tryGetUser(headers);

            Transfer transfer = modelMapper.fromDto(dto);
            transferService.create(transfer,executor);
            return modelMapper.toDto(transfer);
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
