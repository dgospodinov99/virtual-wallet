package com.team01.web.virtualwallet.exceptions;

public class InvalidPasswordException extends RuntimeException{

    public InvalidPasswordException(String message) {
        super(String.format("%s", message));
    }

}
