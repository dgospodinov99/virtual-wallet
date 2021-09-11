package com.team01.web.virtualwallet.exceptions;

public class InvalidTransferException extends RuntimeException{

    public InvalidTransferException(String message) {
        super(String.format("%s", message));
    }

}
