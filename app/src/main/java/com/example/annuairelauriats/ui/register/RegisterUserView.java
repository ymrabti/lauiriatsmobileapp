package com.example.annuairelauriats.ui.register;

public class RegisterUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    RegisterUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
