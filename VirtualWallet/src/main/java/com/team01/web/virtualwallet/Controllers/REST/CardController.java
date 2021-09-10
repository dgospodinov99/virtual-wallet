package com.team01.web.virtualwallet.controllers.REST;

import com.team01.web.virtualwallet.exceptions.DuplicateEntityException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.exceptions.InvalidCardInformation;
import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.dto.CardDto;
import com.team01.web.virtualwallet.services.contracts.CardService;
import com.team01.web.virtualwallet.services.utils.CardModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;
    private final CardModelMapper modelMapper;

    @Autowired
    public CardController(CardService cardService, CardModelMapper modelMapper) {
        this.cardService = cardService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<CardDto> getAll() {
        return cardService.getAll().stream()
                .map(card -> modelMapper.toDto(card))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CardDto getById(@PathVariable int id) {
        try {
            return modelMapper.toDto(cardService.getById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Card create(@RequestHeader HttpHeaders headers, @Valid @RequestBody CardDto dto) {
        try {
//            authenticationHelper.tryGetCustomer(headers);
            Card card = modelMapper.fromDto(dto);
            cardService.create(card);
            return card;
        } catch (InvalidCardInformation e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
