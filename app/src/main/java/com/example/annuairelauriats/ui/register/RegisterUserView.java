package com.example.annuairelauriats.ui.register;

public class RegisterUserView {
    private String displayName;
    private String PassWordUser;
    private String LaureatNon;
    private String LaureatPrenom;
    private String LaureatNumTel;
    private String LaureatImageBase64;

    RegisterUserView(String displayName,String Password,String LaureatNon,String LaureatPrenom,String LaureatNumTel,String LaureatImageBase64) {
        this.displayName = displayName;
        this.PassWordUser=Password;
        this.LaureatNon=LaureatNon;
        this.LaureatPrenom=LaureatPrenom;
        this.LaureatNumTel=LaureatNumTel;
        this.LaureatImageBase64=LaureatImageBase64;
    }

    String getDisplayName() {
        return displayName;
    }

    public String getPassWordUser() {
        return PassWordUser;
    }

    public String getLaureatNom() {
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
