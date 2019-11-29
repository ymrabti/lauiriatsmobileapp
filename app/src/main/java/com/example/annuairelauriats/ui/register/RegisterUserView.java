package com.example.annuairelauriats.ui.register;

class RegisterUserView {
    private String LaureatNom;
    private String LaureatPrenom;
    private String LaureatGender;
    private String LaureatNumTel;
    private String EmailUser;
    private String LaureatPromotion;
    private long LaureatFiliere;
    private String LaureatImageBase64;
    private long Org_selected;
    private String NomOrgEdited;
    private String secteurOrgEdited;
    private String InitulePost;
    private String date_debut_travail_chez_org;
    private String Description;
    private String PassWordUser;

    RegisterUserView(
            String emailUser, String Password, String LaureatNom, String LaureatPrenom, String LaureatNumTel,
            String LaureatImageBase64, String LaureatGender, String LaureatPromotion, long LaureatFiliere,
            long org_selected, String nomOrgEdited, String secteurOrgEdited,String initulePost,
            String date_debut_travail_chez_org_,String description) {
        this.EmailUser = emailUser;this.PassWordUser=Password;this.LaureatNom=LaureatNom;
        this.LaureatPrenom=LaureatPrenom;this.LaureatNumTel=LaureatNumTel;this.LaureatImageBase64=LaureatImageBase64;
        this.LaureatGender=LaureatGender;this.LaureatPromotion=LaureatPromotion;this.LaureatFiliere=LaureatFiliere;
        this.Org_selected=org_selected;this.NomOrgEdited=nomOrgEdited;this.secteurOrgEdited=secteurOrgEdited;
        this.InitulePost=initulePost;this.date_debut_travail_chez_org=date_debut_travail_chez_org_;this.Description=description;
    }

    String getEmailUser() {
        return EmailUser;
    }
    String getPassWordUser() {
        return PassWordUser;
    }
    String getLaureatNom() { return LaureatNom; }
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
    long getLaureatFiliere() { return LaureatFiliere; }
    long getOrg_selected() { return Org_selected; }
    String getDescription() { return Description; }
    String getDate_debut_travail_chez_org() { return date_debut_travail_chez_org; }
    String getInitulePost() { return InitulePost; }
    String getNomOrgEdited() { return NomOrgEdited; }
    String getSecteurOrgEdited() { return secteurOrgEdited; }
}
