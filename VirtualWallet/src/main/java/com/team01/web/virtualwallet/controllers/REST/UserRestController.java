package com.team01.web.virtualwallet.controllers.REST;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.controllers.GlobalExceptionHandler;
import com.team01.web.virtualwallet.exceptions.DuplicateEntityException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.exceptions.InvalidPasswordException;
import com.team01.web.virtualwallet.exceptions.UnauthorizedOperationException;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.*;
import com.team01.web.virtualwallet.models.enums.UserSortOptions;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.TransactionModelMapper;
import com.team01.web.virtualwallet.services.utils.UserModelMapper;
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
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService service;
    private final UserModelMapper modelMapper;
    private final TransactionModelMapper transactionModelMapper;
    private final AuthenticationHelper authenticationHelper;
    private final GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    public UserRestController(UserService service,
                              UserModelMapper modelMapper,
                              TransactionModelMapper transactionModelMapper, AuthenticationHelper authenticationHelper,
                              GlobalExceptionHandler globalExceptionHandler) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.transactionModelMapper = transactionModelMapper;
        this.authenticationHelper = authenticationHelper;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @GetMapping
    public List<UserDto> getAll() {
        return service.getAll().stream()
                .map(modelMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable int id) {
        try {
            return modelMapper.toDto(service.getById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @GetMapping("/filter")
    public List<UserDto> filter(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String sortParam) {
        try {



            var params = new FilterUserParams()
                    .setUsername(username)
                    .setEmail(email)
                    .setPhoneNumber(phoneNumber)
                    .setSortParam(UserSortOptions.valueOfPreview(sortParam));

            return service.filterUsers(params)
                    .stream()
                    .map(user -> modelMapper.toDto(user))
                    .collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/transfers")
    public List<TransactionDto> getUserTransfers(@PathVariable int id,
                                                 @RequestHeader HttpHeaders headers,
                                                 @RequestParam(required = false) String direction) {
        try {
            User executor = authenticationHelper.tryGetUser(headers);

            return service.getUserTransfers(id,executor)
                    .stream()
                    .map(transfer -> transactionModelMapper.toDto(transfer))
                    .collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody CreateUserDto dto, BindingResult result) {
        globalExceptionHandler.checkValidFields(result);
        try {

            User user = modelMapper.fromDto(dto);
            service.create(user);

            return modelMapper.toDto(user);
        } catch (DuplicateEntityException | InvalidPasswordException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable int id, @Valid @RequestBody UpdateUserDto dto, BindingResult result) {
        globalExceptionHandler.checkValidFields(result);
        try {
            User user = modelMapper.fromDto(dto, id);
            service.update(user);
            return modelMapper.toDto(user);
        } catch (DuplicateEntityException | InvalidPasswordException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/block")
    public UserDto blockUser(@RequestBody BlockUserDto dto, @RequestHeader HttpHeaders headers) {
        try {
            User executor = authenticationHelper.tryGetUser(headers);
            User user = service.blockUser(dto.getUsername(), executor);
            return modelMapper.toDto(user);
        } catch (InvalidPasswordException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/unblock")
    public UserDto unblock(@RequestBody BlockUserDto dto, @RequestHeader HttpHeaders headers) {
        try {
            User executor = authenticationHelper.tryGetUser(headers);
            User user = service.unblockUser(dto.getUsername(), executor);
            return modelMapper.toDto(user);
        } catch (InvalidPasswordException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public UserDto delete(@PathVariable int id) {
        try {
            User user = service.getById(id);
            UserDto dto = modelMapper.toDto(user);
            service.delete(id);
            return dto;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
