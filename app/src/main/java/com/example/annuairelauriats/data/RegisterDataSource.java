package com.example.annuairelauriats.data;

import com.example.annuairelauriats.data.model.RegistredUser;

import java.io.IOException;

public class RegisterDataSource {

    public Result<RegistredUser> login(String username, String password) {
if (username.equals("younesmrabti50@gmail.com")){
    try {
        // TODO: handle loggedInUser authentication
        RegistredUser Userlog_in = new RegistredUser(
                java.util.UUID.randomUUID().toString()+"",
                username+"");
        return new Result.Success<>(Userlog_in);
    } catch (Exception e) {
        return new Result.Error(new IOException("Error logging in", e));
    }}
return new Result.Error(new IOException("Error logging in"));
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
