package com.example.annuairelauriats.ui.register;

import androidx.annotation.Nullable;

public class RegisterFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer NomError;
    private Integer PreNomError;
    private Integer NumeroTelError;
    private Integer ImageError;
    private boolean isDataValid;

    RegisterFormState(
            @Nullable Integer usernameError,
            @Nullable Integer passwordError,
            @Nullable Integer nomError,
            Integer preNomError,
            Integer numeroTelError,
            Integer imageError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.NomError = nomError;
        this.PreNomError = preNomError;
        this.NumeroTelError = numeroTelError;
        this.ImageError = imageError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.NomError = null;
        this.PreNomError = null;
        this.NumeroTelError = null;
        this.ImageError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable Integer getUsernameError() {
        return usernameError;
    }
    @Nullable Integer getPasswordError() {
        return passwordError;
    }
    @Nullable Integer getNomError(){return NomError;}
    public Integer getPreNomError() { return PreNomError; }
    public Integer getNumeroTelError() { return NumeroTelError; }
    public Integer getImageError() { return ImageError; }
    boolean isDataValid() {
        return isDataValid;
    }
}
