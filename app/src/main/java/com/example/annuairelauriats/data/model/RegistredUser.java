package com.example.annuairelauriats.data.model;

public class RegistredUser {
    private String userId;
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

    public RegistredUser(
            String userId,
            String emailUser, String Password, String LaureatNom, String LaureatPrenom, String LaureatNumTel,
            String LaureatImageBase64, String LaureatGender, String LaureatPromotion, long LaureatFiliere,
            long org_selected, String nomOrgEdited, String secteurOrgEdited,String initulePost,
            String date_debut_travail_chez_org_,String description) {
        this.userId = userId;
        this.EmailUser = emailUser;this.PassWordUser=Password;this.LaureatNom=LaureatNom;
        this.LaureatPrenom=LaureatPrenom;this.LaureatNumTel=LaureatNumTel;this.LaureatImageBase64=LaureatImageBase64;
        this.LaureatGender=LaureatGender;this.LaureatPromotion=LaureatPromotion;this.LaureatFiliere=LaureatFiliere;
        this.Org_selected=org_selected;this.NomOrgEdited=nomOrgEdited;this.secteurOrgEdited=secteurOrgEdited;
        this.InitulePost=initulePost;this.date_debut_travail_chez_org=date_debut_travail_chez_org_;this.Description=description;
    }

    public String getUserId() { return userId; }
    public String getEmailUser() {
        return EmailUser;
    }
    public String getPassWordUser() {
        return PassWordUser;
    }
    public String getLaureatNom() { return LaureatNom; }
    public String getLaureatPrenom() {
        return LaureatPrenom;
    }
    public String getLaureatNumTel() {
        return LaureatNumTel;
    }
    public String getLaureatImageBase64() {
        return LaureatImageBase64;
    }
    public String getLaureatGender() { return LaureatGender; }
    public String getLaureatPromotion() { return LaureatPromotion; }
    public long getLaureatFiliere() { return LaureatFiliere; }
    public long getOrg_selected() { return Org_selected; }
    public String getDescription() { return Description; }
    public String getDate_debut_travail_chez_org() { return date_debut_travail_chez_org; }
    public String getInitulePost() { return InitulePost; }
    public String getNomOrgEdited() { return NomOrgEdited; }
    public String getSecteurOrgEdited() { return secteurOrgEdited; }
}
