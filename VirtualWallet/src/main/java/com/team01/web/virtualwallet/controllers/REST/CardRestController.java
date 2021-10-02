package com.team01.web.virtualwallet.controllers.REST;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.controllers.GlobalExceptionHandler;
import com.team01.web.virtualwallet.exceptions.*;
import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.CardDto;
import com.team01.web.virtualwallet.models.dto.CreateCardDto;
import com.team01.web.virtualwallet.models.dto.UpdateUserDto;
import com.team01.web.virtualwallet.models.dto.UserDto;
import com.team01.web.virtualwallet.services.contracts.CardService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.CardModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cards")
public class CardRestController {

    private final CardService cardService;
    private final UserService userService;
    private final CardModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;
    private final GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    public CardRestController(CardService cardService,
                              UserService userService,
                              CardModelMapper modelMapper,
                              AuthenticationHelper authenticationHelper,
                              GlobalExceptionHandler globalExceptionHandler) {
        this.cardService = cardService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @GetMapping()
    public List<CardDto> getAll(@RequestHeader HttpHeaders headers) {
        try {
            User executor = authenticationHelper.tryGetUser(headers);
            return cardService.getAll().stream()
                    .map(card -> modelMapper.toDto(card))
                    .collect(Collectors.toList());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public CardDto getById(@PathVariable int id) {
        try {
            return modelMapper.toDto(cardService.getById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public CardDto update(@PathVariable int id, @RequestHeader HttpHeaders headers, @Valid @RequestBody CreateCardDto dto, BindingResult result) {
        globalExceptionHandler.checkValidFields(result);
        try {
            User executor = authenticationHelper.tryGetUser(headers);
            Card card = modelMapper.fromCreateDto(dto, id);

            cardService.update(card, executor);
            return modelMapper.toDto(card);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public CardDto create(@RequestHeader HttpHeaders headers, @Valid @RequestBody CreateCardDto dto, BindingResult result) {
        globalExceptionHandler.checkValidFields(result);
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Card card = modelMapper.fromCreateDto(dto, user);
            cardService.create(card);

            Set<Card> newCardSet =  user.getCards();
            newCardSet.add(card);
            user.setCards(newCardSet);
            userService.update(user);

            return modelMapper.toDto(card);
        } catch (InvalidCardInformation e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public CardDto delete(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {

            User executor = authenticationHelper.tryGetUser(headers);
            Card card = cardService.getById(id);
            CardDto dto = modelMapper.toDto(card);
            cardService.delete(id,executor);
            return dto;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}


