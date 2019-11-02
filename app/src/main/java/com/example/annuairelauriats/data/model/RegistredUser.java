package com.example.annuairelauriats.data.model;

public class RegistredUser {
    private String userId;
    private String displayName;

    public RegistredUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
