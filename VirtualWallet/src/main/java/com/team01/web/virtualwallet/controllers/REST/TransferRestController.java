package com.team01.web.virtualwallet.controllers.REST;

import com.team01.web.virtualwallet.controllers.GlobalExceptionHandler;
import com.team01.web.virtualwallet.exceptions.BadLuckException;
import com.team01.web.virtualwallet.exceptions.DuplicateEntityException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.exceptions.InvalidPasswordException;
import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.dto.TransferDto;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import com.team01.web.virtualwallet.services.utils.TransferModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transfers")
public class TransferRestController {

    private final TransferService transferService;
    private final GlobalExceptionHandler globalExceptionHandler;
    private final TransferModelMapper modelMapper;

    @Autowired
    public TransferRestController(TransferService transferService, GlobalExceptionHandler globalExceptionHandler, TransferModelMapper modelMapper) {
        this.transferService = transferService;
        this.globalExceptionHandler = globalExceptionHandler;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<TransferDto> getAll() {
        return transferService.getAll().stream()
                .map(modelMapper::toDto)
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
    public TransferDto create(@Valid @RequestBody TransferDto dto, BindingResult result) {
        globalExceptionHandler.checkValidFields(result);
        try {
            Transfer transfer = modelMapper.fromDto(dto);
            transferService.create(transfer);

            return modelMapper.toDto(transfer);

        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (BadLuckException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
