package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.InvalidCardInformation;
import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.repositories.contracts.CardRepository;
import com.team01.web.virtualwallet.services.contracts.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public List<Card> getAll() {
        return cardRepository.getAll();
    }

    @Override
    public Card getById(int id) {
        return cardRepository.getById(id);
    }

    @Override
    public Card getByCardNumber(String cardNumber) {
        //validate card number is only digits
        if (cardNumber.matches("[0-9]+") && cardNumber.length() != 16) {
            throw new InvalidCardInformation("Card","number",cardNumber);
        }
        return cardRepository.getByCardNumber(cardNumber);
    }

    @Override
    public void create(Card card) {
        cardRepository.create(card);
    }

    @Override
    public void update(Card card) {
        cardRepository.update(card);
    }

    @Override
    public void delete(int id) {
        cardRepository.delete(id);
    }
}
