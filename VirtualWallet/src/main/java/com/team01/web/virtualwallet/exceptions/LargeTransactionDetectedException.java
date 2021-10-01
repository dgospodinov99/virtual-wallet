package com.team01.web.virtualwallet.exceptions;

public class LargeTransactionDetectedException extends RuntimeException{
    public LargeTransactionDetectedException(String message) {
        super(message);
    }
}
