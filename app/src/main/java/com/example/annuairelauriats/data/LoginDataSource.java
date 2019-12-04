package com.example.annuairelauriats.data;

import com.example.annuairelauriats.data.model.LoggedInUser;

import java.io.IOException;

import static com.example.annuairelauriats.ui.home.Classtest.is_login_valid;

public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        try {
            if (is_login_valid(null,username,password))
            {


                LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");return new Result.Success<>(fakeUser);


            }
            else{return new Result.Error(new IOException("Error logging in"));}
        } catch (Exception e) {
            e.printStackTrace();return new Result.Error(new IOException("Erreur log in"));
        }

    }

    public void logout() {
    }

}
