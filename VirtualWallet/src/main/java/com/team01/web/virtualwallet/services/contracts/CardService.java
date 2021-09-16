package com.team01.web.virtualwallet.services.contracts;

import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.User;

import java.util.List;

public interface CardService {

    List<Card> getAll(User executor);

    Card getById(int id);

    Card getByCardNumber(String cardName);

    void create(Card card);

    void update(Card card, User executor);

    void delete(int id, User executor);
}
