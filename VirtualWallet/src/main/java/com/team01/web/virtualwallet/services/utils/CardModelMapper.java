package com.team01.web.virtualwallet.services.utils;

import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.dto.CardDto;
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

    public Card fromDto(CardDto dto) {
        Card card = new Card();
        card.setCardNumber(dto.getCardNumber());
        card.setCheckNumber(dto.getCheckNumber());
        card.setHolder(dto.getHolder());
        card.setUser(userService.getById(4));

        //todo fix - db card user_id has defaulf value

        //set card userId from userService
        return card;
    }
}
