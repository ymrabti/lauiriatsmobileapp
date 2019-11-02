package com.example.annuairelauriats.data;

import com.example.annuairelauriats.data.model.RegistredUser;

import java.io.IOException;

public class RegisterDataSource {

    public Result<RegistredUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            RegistredUser fakeUser =
                    new RegistredUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
