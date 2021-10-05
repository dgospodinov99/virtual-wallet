package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.DuplicateEntityException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.exceptions.UnauthorizedOperationException;
import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.repositories.contracts.CardRepository;
import com.team01.web.virtualwallet.services.contracts.CardService;
import com.team01.web.virtualwallet.services.utils.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl extends BaseGetServiceImpl<Card> implements CardService {

    private static final String ONLY_DIGITS = "[0-9]+";
    private static final String INVALID_CARD_OWNER = "Invalid card owner!";
    private static final String NOT_ADMIN_MESSAGE = "Only administrators can do this operation";
    private static final String CARD_ONLY_DIGITS_MESSAGE = "Card number can be only digits";

    private final CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, CardRepository cardRepository1) {
        super(cardRepository);
        this.cardRepository = cardRepository1;
    }

    @Override
    public Card getByCardNumber(String cardNumber) {
        return cardRepository.getByCardNumber(cardNumber);
    }

    @Override
    public List<Card> getUserCards(User user, User executor) {
        if(!executor.isAdmin() && user.getId() != executor.getId()){
            throw new UnauthorizedOperationException(INVALID_CARD_OWNER);
        }
        return cardRepository.getUserCards(user.getId());
    }

    @Override
    public void create(Card card) {
        verifyUniqueCardNumber(card);
        cardRepository.create(card);
    }

    @Override
    public void update(Card card, User executor) {
        Helpers.validateUserIsCardOwner(executor,card);
        verifyUniqueCardNumber(card);
        cardRepository.update(card);
    }

    @Override
    public void delete(int id, User executor) {
        Card card = getById(id);
        Helpers.validateUserIsCardOwner(executor,card);
        card.setActive(false);

        cardRepository.update(card);
    }

    private void verifyUniqueCardNumber(Card card) {
        boolean duplicateCardNumberExists = true;
        try {
            Card duplicate = getByCardNumber(card.getCardNumber());
            if(duplicate.getId() == card.getId()){
                duplicateCardNumberExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateCardNumberExists = false;
        }

        if (duplicateCardNumberExists) {
            throw new DuplicateEntityException("Card", "number", card.getCardNumber());
        }
    }
}
