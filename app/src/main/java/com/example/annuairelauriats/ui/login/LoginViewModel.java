package com.example.annuairelauriats.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Intent;
import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;
import static com.example.annuairelauriats.ui.home.Classtest.getJsonObjectBykey;
import static com.example.annuairelauriats.ui.home.Classtest.id_connected;
import static com.example.annuairelauriats.ui.home.Classtest.is_email_exist;
import static com.example.annuairelauriats.ui.home.Classtest.is_password_correct;
import static com.example.annuairelauriats.ui.home.Classtest.laureats;
import static com.example.annuairelauriats.ui.home.Classtest.setPref;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    LiveData<LoginFormState> getLoginFormState() { return loginFormState; }
    public void login(String username, String password) {
        try {
            if (!is_email_exist(LoginActivity.getContexte(),username)){
                loginFormState.setValue(new LoginFormState(R.string.invalid_doesnotexist, null));
            }
            else {
                if (!is_password_correct(LoginActivity.getContexte(),username,password)){
                    loginFormState.setValue(new LoginFormState(null, R.string.wrong_password));
                }
                else{
                    id_connected = getJsonObjectBykey(LoginActivity.getContexte(),"email",username,laureats).getInt("id");
                    Intent i = new Intent(LoginActivity.getContexte(), MainActivity.class);
                    setPref(LoginActivity.getContexte(),id_connected);
                    LoginActivity.getContexte().startActivity(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }
    private boolean isUserNameValid(String username) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
