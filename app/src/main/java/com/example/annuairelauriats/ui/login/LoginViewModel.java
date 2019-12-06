package com.example.annuairelauriats.ui.login;

import androidx.lifecycle.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.util.Patterns;
import android.widget.Toast;

import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.data.LoginDataSource;
import com.example.annuairelauriats.data.Result;
import com.example.annuairelauriats.data.model.LoggedInUser;
import com.example.annuairelauriats.R;

import static com.example.annuairelauriats.ui.home.Classtest.getJsonObjectBykey;
import static com.example.annuairelauriats.ui.home.Classtest.id_connected;
import static com.example.annuairelauriats.ui.home.Classtest.is_email_exist;
import static com.example.annuairelauriats.ui.home.Classtest.is_password_correct;
import static com.example.annuairelauriats.ui.home.Classtest.laureats;
import static com.example.annuairelauriats.ui.home.Classtest.setPref;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private LoginResult loginResult ;
    private LoginDataSource loginDataSource;
    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }
    public LoginResult getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        /**Result<LoggedInUser> result = loginDataSource.login(username, password);
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult = new LoginResult(data);
        } else {
            loginResult=new LoginResult(100);
        }
         */
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

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
    private boolean isemailexist(String email){
        return email.equals("younesmrabti50@gmail.com");
    }
    private boolean iscomibinationvalid(String email,String password){
        return email.equals("younesmrabti50@gmail.com") && password.equals("123123");
    }
    /**LoginViewModel(LoginDataSource loginDataSource) {
     this.loginDataSource = loginDataSource;
     }
     */
    /**private LoginRepository loginRepository;
     LoginViewModel(LoginRepository loginRepository) {
     this.loginRepository = loginRepository;
     }

     */
}
