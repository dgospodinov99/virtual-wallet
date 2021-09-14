package com.team01.web.virtualwallet.exceptions;

public class BlockedUserException extends RuntimeException {

    public BlockedUserException(String message) {
        super(String.format("%s", message));
    }
}
