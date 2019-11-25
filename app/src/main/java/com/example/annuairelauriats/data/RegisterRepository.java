package com.example.annuairelauriats.data;

import com.example.annuairelauriats.data.model.RegistredUser;

public class RegisterRepository {
    private static volatile RegisterRepository instance;
    private RegisterDataSource dataSource;
    private RegistredUser user = null;
    private RegisterRepository(RegisterDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RegisterRepository getInstance(RegisterDataSource dataSource) {
        if (instance == null) {
            instance = new RegisterRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setRegistredUser(RegistredUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<RegistredUser> Register(
            String username,
            String password,
            String Nom,
            String PreNom,
            String NumTele,
            String ImageBase64,
            String LaureatGender,
            String LaureatPromotion,
            String LaureatFiliere,
            String LaureatOrg) {
        // handle login
        Result<RegistredUser> result = dataSource.RegisterUtilisateur(username, password,Nom,PreNom,NumTele,ImageBase64,
                LaureatGender,
                LaureatPromotion,
                LaureatFiliere,
                LaureatOrg);
        if (result instanceof Result.Success) {
            setRegistredUser(  ((Result.Success<RegistredUser>) result).getData() );
        }
        return result;
    }
}
