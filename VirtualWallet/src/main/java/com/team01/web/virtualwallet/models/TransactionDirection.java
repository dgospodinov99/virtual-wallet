package com.team01.web.virtualwallet.models;

public enum TransactionDirection {
    IN,
    OUT;

    @Override
    public String toString() {
        switch (this) {
            case IN:
                return "in";
            case OUT:
                return "out";
            default:
                throw new IllegalArgumentException("Invalid transfer direction (can be in/out).");
        }
    }
}
