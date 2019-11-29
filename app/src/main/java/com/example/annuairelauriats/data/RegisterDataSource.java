package com.example.annuairelauriats.data;

import com.example.annuairelauriats.data.model.RegistredUser;

import java.io.IOException;

public class RegisterDataSource {

    public Result<RegistredUser> RegisterUtilisateur(
            String emailUser, String Password, String LaureatNom, String LaureatPrenom, String LaureatNumTel,
            String LaureatImageBase64, String LaureatGender, String LaureatPromotion, long LaureatFiliere,
            long org_selected, String nomOrgEdited, String secteurOrgEdited,String initulePost,
            String date_debut_travail_chez_org_,String description)
    {
        try
        {
            // TODO: handle loggedInUser authentication
            RegistredUser UserRegister = new RegistredUser(
                    java.util.UUID.randomUUID().toString()+"",
                    emailUser, Password, LaureatNom, LaureatPrenom, LaureatNumTel,
                    LaureatImageBase64, LaureatGender, LaureatPromotion, LaureatFiliere,
                    org_selected, nomOrgEdited, secteurOrgEdited,initulePost,
                    date_debut_travail_chez_org_,description);
            return new Result.Success<>(UserRegister);
        } catch (Exception e)
        {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
