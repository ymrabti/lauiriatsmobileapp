package com.example.annuairelauriats.data;

import com.example.annuairelauriats.data.model.RegistredUser;

import java.io.IOException;

public class RegisterDataSource {

    public Result<RegistredUser> RegisterUtilisateur(
            String username,
            String password,
            String Nom,
            String PreNom,
            String NumTele,
            String ImageBase64)
    {
        try
        {
            // TODO: handle loggedInUser authentication
            RegistredUser UserRegister = new RegistredUser(
                    java.util.UUID.randomUUID().toString()+"",
                    username+"",
                    password+"",
                    Nom+"",
                    PreNom+"",
                    NumTele+"",
                    ImageBase64+"");
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
