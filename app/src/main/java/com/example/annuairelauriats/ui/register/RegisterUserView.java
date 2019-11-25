package com.example.annuairelauriats.ui.register;

class RegisterUserView {
    private String displayName;
    private String PassWordUser;
    private String LaureatNon;
    private String LaureatPrenom;
    private String LaureatNumTel;
    private String LaureatImageBase64;
    private String LaureatGender;
    private String LaureatPromotion;
    private String LaureatFiliere;
    private String LaureatOrganisation;

    RegisterUserView(
            String displayName,
            String Password,
            String LaureatNon,
            String LaureatPrenom,
            String LaureatNumTel,
            String LaureatImageBase64,
            String LaureatGender,
            String LaureatPromotion,
            String LaureatFiliere,
            String LaureatOrg) {
        this.displayName = displayName;
        this.PassWordUser=Password;
        this.LaureatNon=LaureatNon;
        this.LaureatPrenom=LaureatPrenom;
        this.LaureatNumTel=LaureatNumTel;
        this.LaureatImageBase64=LaureatImageBase64;
        this.LaureatGender=LaureatGender;
        this.LaureatPromotion=LaureatPromotion;
        this.LaureatFiliere=LaureatFiliere;
        this.LaureatOrganisation=LaureatOrg;
    }

    String getDisplayName() {
        return displayName;
    }
    String getPassWordUser() {
        return PassWordUser;
    }
    String getLaureatNom() { return LaureatNon; }
    String getLaureatPrenom() {
        return LaureatPrenom;
    }
    String getLaureatNumTel() {
        return LaureatNumTel;
    }
    String getLaureatImageBase64() {
        return LaureatImageBase64;
    }
    String getLaureatGender() { return LaureatGender; }
    String getLaureatPromotion() { return LaureatPromotion; }
    String getLaureatFiliere() { return LaureatFiliere; }
    String getLaureatOrganisation() { return LaureatOrganisation; }
}
