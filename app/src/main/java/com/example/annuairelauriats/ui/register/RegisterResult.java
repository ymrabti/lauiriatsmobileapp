package com.example.annuairelauriats.ui.register;

import androidx.annotation.Nullable;

public class RegisterResult {
    @Nullable
    private RegisterUserView success;
    @Nullable
    private Integer error;

    RegisterResult(@Nullable Integer error) {
        this.error = error;
    }

    RegisterResult(@Nullable RegisterUserView success) {
        this.success = success;
    }

    @Nullable
    RegisterUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
