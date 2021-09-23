package com.team01.web.virtualwallet.models.enums;

public enum TransactionDirection {
    IN,
    OUT;

    @Override
    public String toString() {
        switch (this) {
            case IN:
                return "Income";
            case OUT:
                return "Outcome";
            default:
                throw new IllegalArgumentException("Invalid transfer direction (can be in/out).");
        }
    }
}
