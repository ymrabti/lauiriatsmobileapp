package com.example.annuairelauriats.ui.register;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.annuairelauriats.R;
import com.example.annuairelauriats.data.RegisterRepository;
import com.example.annuairelauriats.data.Result;
import com.example.annuairelauriats.data.model.RegistredUser;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private RegisterRepository registerRepository;

    RegisterViewModel(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    LiveData<RegisterFormState> getLoginFormState() {
        return registerFormState;
    }

    LiveData<RegisterResult> getLoginResult() {
        return registerResult;
    }

    public void register(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<RegistredUser> result = registerRepository.Register(username, password);

        if (result instanceof Result.Success) {
            RegistredUser data = ((Result.Success<RegistredUser>) result).getData();
            //registerResult.setValue(new RegisterResult(new RegistredUser(data.getUserId(),data.getDisplayName())));
        } else {
            registerResult.setValue(new RegisterResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_password));
        } else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
