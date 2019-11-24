package com.example.annuairelauriats.data.model;

public class RegistredUser {
    private String userId;
    private String displayName;
    private String PassWordUser;
    private String LaureatNon;
    private String LaureatPrenom;
    private String LaureatNumTel;
    private String LaureatImageBase64;

    public RegistredUser(String userId, String displayName,String Password,String LaureatNon,String LaureatPrenom,String LaureatNumTel,String LaureatImageBase64) {
        this.userId = userId;
        this.displayName = displayName;
        this.PassWordUser=Password;
        this.LaureatNon=LaureatNon;
        this.LaureatPrenom=LaureatPrenom;
        this.LaureatNumTel=LaureatNumTel;
        this.LaureatImageBase64=LaureatImageBase64;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPassWordUser() {
        return PassWordUser;
    }

    public String getLaureatNon() {
        return LaureatNon;
    }

    public String getLaureatPrenom() {
        return LaureatPrenom;
    }

    public String getLaureatNumTel() {
        return LaureatNumTel;
    }

    public String getLaureatImageBase64() {
        return LaureatImageBase64;
    }
}
