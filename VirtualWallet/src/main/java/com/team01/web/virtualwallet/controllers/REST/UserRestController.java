package com.team01.web.virtualwallet.controllers.REST;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.exceptions.DuplicateEntityException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.exceptions.InvalidPasswordException;
import com.team01.web.virtualwallet.exceptions.UnauthorizedOperationException;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.BlockUserDto;
import com.team01.web.virtualwallet.models.dto.CreateUserDto;
import com.team01.web.virtualwallet.models.dto.UpdateUserDto;
import com.team01.web.virtualwallet.models.dto.UserDto;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserRestController(UserService service, UserModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
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

    @PostMapping
    public UserDto create(@Valid @RequestBody CreateUserDto dto) {
        try {
            User user = modelMapper.fromCreateDto(dto);
            service.create(user);

            return modelMapper.toDto(user);
        } catch (DuplicateEntityException | InvalidPasswordException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable int id, @Valid @RequestBody UpdateUserDto dto) {
        try {
            User user = modelMapper.fromUpdateDto(dto, id);
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
