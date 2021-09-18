package com.team01.web.virtualwallet.models.enums;

import java.util.HashMap;
import java.util.Map;

public enum TransactionSortOptions {

    AMOUNT_ASC("Amount, ascending", "order by amount asc"),
    AMOUNT_DESC("Amount, descending", "order by amount desc"),
    DATE_ASC("Date, from oldest", "order by date asc"),
    DATE_DESC("Date, from most recent", "order by date desc");

    private final String query;
    private final String preview;

    private static final Map<String, TransactionSortOptions> BY_PREVIEW = new HashMap<>();

    static {
        for (TransactionSortOptions e : values()) {
            BY_PREVIEW.put(e.preview, e);
        }
    }

    public static TransactionSortOptions valueOfPreview(String preview) {
        return BY_PREVIEW.get(preview);
    }

    TransactionSortOptions(String preview, String query) {
        this.preview = preview;
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public String getPreview() {
        return preview;
    }
}
