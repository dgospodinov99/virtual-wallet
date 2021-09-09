package com.team01.web.virtualwallet.exceptions;

public class DuplicateEntityException extends RuntimeException {

    public DuplicateEntityException(String message) {
        super(String.format("%s", message));
    }

    public DuplicateEntityException(String type, String attribute, String value) {
        super(String.format("%s with %s %s already exists.", type, attribute, value));
    }

    public DuplicateEntityException(String type, String attribute1, String value, String attribute2, String value2) {
        super(String.format("%s with %s %s and %s %s already exists.", type, attribute1, value, attribute2, value2));
    }
}
