package com.example.annuairelauriats.data;

import com.example.annuairelauriats.data.model.LoggedInUser;

public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        if (username.equals("younesmrabti50@gmail.com")&&password.equals("123123")){
            LoggedInUser User_connected = new LoggedInUser(java.util.UUID.randomUUID().toString(), username);
            return new Result.Success<>(User_connected);
        }
        else{
            return new Result.ErroOr(new Exception("Erreur log in") );
        }

    }

    public void logout() {
    }

}
