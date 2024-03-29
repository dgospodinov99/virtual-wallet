package com.team01.web.virtualwallet.repositories.contracts;

import com.team01.web.virtualwallet.models.Card;

import java.util.List;

public interface CardRepository extends BaseGetRepository<Card>, BaseModifyRepository<Card>{

    Card getByCardNumber(String cardNumber);

    List<Card> getUserCards(int id);
}
