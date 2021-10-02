package com.team01.web.virtualwallet.repositories.contracts;

import com.team01.web.virtualwallet.models.Card;

import java.util.List;

public interface CardRepository extends BaseGetRepository<Card>{

    Card getByCardNumber(String cardNumber);

    void create(Card card);

    void update(Card card);

    void delete(int id);

}
