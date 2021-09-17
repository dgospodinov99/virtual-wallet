package com.team01.web.virtualwallet.services.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Helpers {

    public static Optional<LocalDateTime> stringToLocalDateTimeOptional(String date) {
        if (date == null) {
            return Optional.empty();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Optional.of(LocalDateTime.parse(date, formatter));
    }
}
