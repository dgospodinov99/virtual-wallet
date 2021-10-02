package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.DuplicateEntityException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.exceptions.InvalidCardInformation;
import com.team01.web.virtualwallet.exceptions.UnauthorizedOperationException;
import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.repositories.contracts.CardRepository;
import com.team01.web.virtualwallet.services.contracts.CardService;
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
        validateCardNumber(cardNumber);
        return cardRepository.getByCardNumber(cardNumber);
    }

    @Override
    public void create(Card card) {
        verifyUniqueCardNumber(card);
        validateCardNumber(card.getCardNumber());
        cardRepository.create(card);
    }

    @Override
    public void update(Card card, User executor) {
        validateUser(executor, card);
        validateCardNumber(card.getCardNumber());
        cardRepository.update(card);
    }

    @Override
    public void delete(int id, User executor) {
        Card card = getById(id);
        validateUser(executor, card);
        card.setActive(false);

        cardRepository.update(card);
    }

    private void verifyUniqueCardNumber(Card card) {
        boolean duplicateCardNumberExists = true;
        try {
            getByCardNumber(card.getCardNumber());
        } catch (EntityNotFoundException e) {
            duplicateCardNumberExists = false;
        }

        if (duplicateCardNumberExists) {
            throw new DuplicateEntityException("Card", "number", card.getCardNumber());
        }
    }

    private void validateUser(User executor, Card card) {
        if (!executor.isAdmin() && card.getUser().getId() != executor.getId()) {
            throw new UnauthorizedOperationException(INVALID_CARD_OWNER);
        }
    }

    private void validateAdmin(User executor) {
        if (!executor.isAdmin()) {
            throw new UnauthorizedOperationException(NOT_ADMIN_MESSAGE);
        }
    }

    private void validateCardNumber(String cardNumber) {
        if (!cardNumber.matches(ONLY_DIGITS) || cardNumber.length() != 16) { //validates card number is only digits
            throw new InvalidCardInformation(CARD_ONLY_DIGITS_MESSAGE);
        }
    }
}
