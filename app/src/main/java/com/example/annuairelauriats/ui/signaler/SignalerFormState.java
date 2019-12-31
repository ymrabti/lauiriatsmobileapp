package com.example.annuairelauriats.ui.signaler;

import androidx.annotation.Nullable;

public class SignalerFormState  {
    @Nullable
    private Integer NomError;
    private Integer PreNomError;
    private Integer genderError;
    private Integer filiereError;
    private Integer promotionError;
    private Integer orgError;
    private Integer secteurError;
    private Integer NumeroTelError;
    @Nullable private Integer usernameError;
    private Integer ImageError;
    @Nullable private Integer passwordError;
    private Integer radioError;
    private Integer nv_org_nom_Error;
    private Integer pickError;
    private Integer dateError;
    private Integer intituleError;
    private Integer descError;
    private boolean isDataValid;

    SignalerFormState(
            @Nullable Integer nomError,
            Integer preNomError,
            Integer numeroTelError,
            @Nullable Integer emailError,
            Integer imageError,
            @Nullable Integer passwordError,
            Integer radioError,
            Integer nv_org_nom_Error,
            Integer pickError,
            Integer dateError,
            Integer intituleErrorError,
            Integer descError,
            Integer genderError,
            Integer filiereError,
            Integer promotionError,
            Integer orgError,
            Integer secteurError) {
        this.NomError = nomError;
        this.PreNomError = preNomError;
        this.NumeroTelError = numeroTelError;
        this.usernameError = emailError;
        this.ImageError = imageError;
        this.passwordError = passwordError;
        this.radioError=radioError;
        this.nv_org_nom_Error=nv_org_nom_Error;
        this.pickError=pickError;
        this.dateError=dateError;
        this.intituleError=intituleErrorError;
        this.descError=descError;
        this.genderError=genderError;
        this.filiereError=filiereError;
        this.promotionError=promotionError;
        this.orgError=orgError;
        this.secteurError=secteurError;
        this.isDataValid = false;
    }

    SignalerFormState(boolean isDataValid) {
        this.NomError = null;
        this.PreNomError = null;
        this.NumeroTelError = null;
        this.usernameError = null;
        this.ImageError = null;
        this.passwordError = null;
        this.radioError=null;
        this.nv_org_nom_Error=null;
        this.pickError=null;
        this.dateError=null;
        this.intituleError=null;
        this.descError=null;
        this.genderError=null;
        this.filiereError=null;
        this.promotionError=null;
        this.orgError=null;
        this.secteurError=null;
        this.isDataValid = isDataValid;
    }

    @Nullable Integer getUsernameError() {
        return usernameError;
    }
    @Nullable Integer getPasswordError() {
        return passwordError;
    }
    @Nullable Integer getNomError(){return NomError;}
    Integer getPreNomError() { return PreNomError; }
    Integer getNumeroTelError() { return NumeroTelError; }
    Integer getImageError() { return ImageError; }

    Integer getDateError() {
        return dateError;
    }

    Integer getNv_org_nom_Error() {
        return nv_org_nom_Error;
    }

    Integer getDescError() {
        return descError;
    }

    Integer getRadioError() {
        return radioError;
    }

    Integer getIntituleError() {
        return intituleError;
    }

    Integer getPickError() {
        return pickError;
    }

    public Integer getOrgError() {
        return orgError;
    }

    public Integer getFiliereError() {
        return filiereError;
    }

    public Integer getGenderError() {
        return genderError;
    }

    public Integer getPromotionError() {
        return promotionError;
    }

    public Integer getSecteurError() {
        return secteurError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
