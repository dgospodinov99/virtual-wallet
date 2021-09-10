package com.team01.web.virtualwallet.services.utils;

import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.dto.CardDto;
import com.team01.web.virtualwallet.services.contracts.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardModelMapper {

    private final CardService cardService;

    @Autowired
    public CardModelMapper(CardService cardService) {
        this.cardService = cardService;
    }

    public Card fromDto(CardDto dto) {
        Card card = new Card();
        card.setCardNumber(dto.getCardNumber());
        card.setCheckNumber(dto.getCheckNumber());
        card.setHolder(dto.getHolder());

        //todo fix - db card user_id has defaulf value
        //set card userId from userService
        return card;
    }
}
