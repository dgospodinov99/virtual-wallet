package com.team01.web.virtualwallet.services.contracts;

import com.team01.web.virtualwallet.models.Card;

import java.util.List;

public interface CardService {

    List<Card> getAll();

    Card getById(int id);

    Card getByCardNumber(String cardName);

    void create(Card card);

    void update(Card card);

    void delete(int id);
}
