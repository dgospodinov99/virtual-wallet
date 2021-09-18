package com.team01.web.virtualwallet.services.utils;

import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.CardDto;
import com.team01.web.virtualwallet.models.dto.CreateCardDto;
import com.team01.web.virtualwallet.services.contracts.CardService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Component
public class CardModelMapper {

    private final CardService cardService;
    private final UserService userService;

    @Autowired
    public CardModelMapper(CardService cardService, UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    public Card fromCreateDto(CreateCardDto dto, int id) {
        Card card = cardService.getById(id);
        dtoToObject(dto, card);
        return card;
    }

    public Card fromCreateDto(CreateCardDto dto, User user) {
        Card card = new Card();
        dtoToObject(dto, card);
        card.setUser(user);
        return card;
    }

    public CardDto toDto(Card card) {
        CardDto dto = new CardDto();
        dto.setCardNumber(card.getCardNumber());
        dto.setCheckNumber(card.getCheckNumber());
        dto.setHolder(card.getHolder());
        dto.setExpirationDate(card.getExpirationDate());
        dto.setUserId(card.getUser().getId());
        return dto;
    }

    private void dtoToObject(CreateCardDto dto, Card card) {
        card.setCardNumber(dto.getCardNumber());
        card.setCheckNumber(dto.getCheckNumber());
        card.setHolder(dto.getHolder());


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        YearMonth date = YearMonth.parse(dto.getExpirationDate(), formatter);
        card.setExpirationDate(date.atEndOfMonth());
    }


}
