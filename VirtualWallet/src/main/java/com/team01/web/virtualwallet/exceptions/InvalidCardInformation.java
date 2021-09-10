package com.team01.web.virtualwallet.exceptions;

public class InvalidCardInformation extends RuntimeException {

    public InvalidCardInformation(String type, String attribute, String value) {
        super(String.format("Invalid %s %s - %s.", type, attribute, value));
    }

    public InvalidCardInformation(String value) {
        super(String.format("%s", value));
    }
}