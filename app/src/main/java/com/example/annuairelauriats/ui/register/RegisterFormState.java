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
    private Integer genderError;
    private Integer promoError;
    private Integer filiereError;
    private Integer orgError;
    private boolean isDataValid;

    public RegisterFormState(
            @Nullable Integer usernameError,
            @Nullable Integer passwordError,
            @Nullable Integer nomError,
            Integer preNomError,
            Integer numeroTelError,
            Integer imageError,
            Integer genderError,
            Integer promoError,
            Integer filiereError,
            Integer orgError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.NomError = nomError;
        this.PreNomError = preNomError;
        this.NumeroTelError = numeroTelError;
        this.ImageError = imageError;
        this.genderError=genderError;
        this.promoError=promoError;
        this.filiereError=filiereError;
        this.orgError=orgError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.NomError = null;
        this.PreNomError = null;
        this.NumeroTelError = null;
        this.ImageError = null;
        this.genderError=null;
        this.promoError=null;
        this.filiereError=null;
        this.orgError=null;
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

    public Integer getGenderError() {
        return genderError;
    }

    public Integer getPromoError() {
        return promoError;
    }

    public Integer getFiliereError() {
        return filiereError;
    }

    public Integer getOrgError() {
        return orgError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
