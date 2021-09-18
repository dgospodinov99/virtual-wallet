package com.team01.web.virtualwallet.models.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserSortOptions {

    USERNAME_ASC("Username, ascending", "order by username asc"),
    USERNAME_DESC("Username, descending", "order by username desc"),
    EMAIL_ASC("Email, ascending", "order by email asc"),
    EMAIL_DESC("Email, descending", "order by email desc");

    private final String query;
    private final String preview;

    private static final Map<String, UserSortOptions> BY_PREVIEW = new HashMap<>();

    static {
        for (UserSortOptions e : values()) {
            BY_PREVIEW.put(e.preview, e);
        }
    }

    public static UserSortOptions valueOfPreview(String preview) {
        return BY_PREVIEW.get(preview);
    }

    UserSortOptions(String preview, String query) {
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
