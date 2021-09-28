package com.team01.web.virtualwallet.models.enums;

public enum TransactionDirection {
    IN,
    OUT;

    @Override
    public String toString() {
        switch (this) {
            case IN:
                return "Incoming";
            case OUT:
                return "Outgoing";
            default:
                throw new IllegalArgumentException("Invalid transfer direction (can be in/out).");
        }
    }
}
