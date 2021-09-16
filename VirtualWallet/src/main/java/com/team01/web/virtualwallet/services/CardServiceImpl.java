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
public class CardServiceImpl implements CardService {

    private static final String ONLY_DIGITS = "[0-9]+";
    private static final String INVALID_CARD_OWNER = "Invalid card owner!";
    private static final String NOT_ADMIN_MESSAGE = "Only administrators can do this operation";

    private final CardRepository cardRepository;


    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public List<Card> getAll(User executor) {
        validateAdmin(executor);
        return cardRepository.getAll();
    }

    @Override
    public Card getById(int id) {
        return cardRepository.getById(id);
    }

    @Override
    public Card getByCardNumber(String cardNumber) {
        validateCardNumber(cardNumber);
        return cardRepository.getByCardNumber(cardNumber);
    }


    @Override
    public void create(Card card) {
        boolean duplicateCardNumberExists = true;
        try {
            getByCardNumber(card.getCardNumber());
        } catch (EntityNotFoundException e) {
            duplicateCardNumberExists = false;
        }

        if (duplicateCardNumberExists) {
            throw new DuplicateEntityException("Card", "number", card.getCardNumber());
        }
        cardRepository.create(card);
    }

    @Override
    public void update(Card card) {
        cardRepository.update(card);
    }

    @Override
    public void delete(int id, User executor) {
        Card card = getById(id);
        validateUser(executor, card);
        card.setActive(false);

        cardRepository.update(card);
    }

    private void validateUser(User executor, Card card) {
        if(!executor.isAdmin() && card.getUser().getId() != executor.getId()){
            throw new UnauthorizedOperationException(INVALID_CARD_OWNER);
        }
    }

    private void validateAdmin(User executor) {
        if(!executor.isAdmin()){
            throw new UnauthorizedOperationException(NOT_ADMIN_MESSAGE);
        }
    }

    private void validateCardNumber(String cardNumber) {
        if (!cardNumber.matches(ONLY_DIGITS) || cardNumber.length() != 16) { //validates card number is only digits
            throw new InvalidCardInformation("Card", "number", cardNumber);
        }
    }
}
