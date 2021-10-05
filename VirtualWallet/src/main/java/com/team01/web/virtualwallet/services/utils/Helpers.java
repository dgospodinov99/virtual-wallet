package com.team01.web.virtualwallet.services.utils;

import com.team01.web.virtualwallet.exceptions.UnauthorizedOperationException;
import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Helpers {
    private static final String INVALID_CARD_OWNER = "Invalid card owner!";

    public static Optional<LocalDateTime> stringToLocalDateTimeOptional(String date) {
        if (date == null) {
            return Optional.empty();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Optional.of(LocalDateTime.parse(date, formatter));
    }

    public static void validateUserIsCardOwner(User executor, Card card) {
        if (!executor.isAdmin() && card.getUser().getId() != executor.getId()) {
            throw new UnauthorizedOperationException(INVALID_CARD_OWNER);
        }
    }
}
