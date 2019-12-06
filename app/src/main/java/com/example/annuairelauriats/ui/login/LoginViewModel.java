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

    /**LoginViewModel(LoginDataSource loginDataSource) {
        this.loginDataSource = loginDataSource;
    }
*/
    public void login(String username, String password) {
        /**Result<LoggedInUser> result = loginDataSource.login(username, password);
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult = new LoginResult(data);
        } else {
            loginResult=new LoginResult(100);
        }
         */
        if (!isemailexist(username)){
            loginFormState.setValue(new LoginFormState(R.string.invalid_doesnotexist, null));
        }
        else {
            if (!(username.equals("younesmrabti50@gmail.com") && password.equals("111111"))){
                loginFormState.setValue(new LoginFormState(null, R.string.wrong_password));
            }
            else{
                Toast.makeText(LoginActivity.getContexte(),"successfull",Toast.LENGTH_LONG).show();
                /**Intent i = new Intent(LoginActivity.getContexte(), MainActivity.class);
                LoginActivity.getContexte().startActivity(i);*/
            }
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
    /**private LoginRepository loginRepository;
     LoginViewModel(LoginRepository loginRepository) {
     this.loginRepository = loginRepository;
     }

     */
}
