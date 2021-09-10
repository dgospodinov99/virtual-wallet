package com.team01.web.virtualwallet.services.utils;

import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.CardDto;
import com.team01.web.virtualwallet.models.dto.CreateCardDto;
import com.team01.web.virtualwallet.services.contracts.CardService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardModelMapper {

    private final CardService cardService;
    private final UserService userService;

    @Autowired
    public CardModelMapper(CardService cardService, UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    public Card fromCreateDto(CreateCardDto dto, User user) {
        Card card = new Card();
        card.setCardNumber(dto.getCardNumber());
        card.setCheckNumber(dto.getCheckNumber());
        card.setHolder(dto.getHolder());
        card.setUser(user);

        return card;
    }

    public CardDto toDto(Card card) {
        CardDto dto = new CardDto();
        dto.setCardNumber(card.getCardNumber());
        dto.setCheckNumber(card.getCheckNumber());
        dto.setHolder(card.getHolder());
        dto.setUserId(card.getUser().getId());
        return dto;
    }

}
