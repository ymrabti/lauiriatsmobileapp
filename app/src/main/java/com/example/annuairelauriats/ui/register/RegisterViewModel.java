package com.example.annuairelauriats.ui.register;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.annuairelauriats.R;
import com.example.annuairelauriats.data.RegisterRepository;
import com.example.annuairelauriats.data.Result;
import com.example.annuairelauriats.data.model.RegistredUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private RegisterRepository registerRepository;

    RegisterViewModel(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    LiveData<RegisterFormState> getREgisterFormState() {
        return registerFormState;
    }

    LiveData<RegisterResult> getRegistreResult() {
        return registerResult;
    }

    public void register(
            String username,
            String password,
            String Nom,
            String PreNom,
            String NumTele,
            String ImageBase64,
            String gender,
            String promotion,
            String filiere,
            String organisme) {
        // can be launched in a separate asynchronous job
        Result<RegistredUser> result = registerRepository.Register(
                username,
                password,
                Nom,
                PreNom,
                NumTele,
                ImageBase64,
                gender,
                promotion,
                filiere,
                organisme);

        if (result instanceof Result.Success) {
            RegistredUser data = ((Result.Success<RegistredUser>) result).getData();
            registerResult.setValue(new RegisterResult(new RegisterUserView(
                    data.getDisplayName()+"",
                    data.getPassWordUser()+"",
                    data.getLaureatNon()+"",
                    data.getLaureatPrenom()+"",
                    data.getLaureatNumTel()+"",
                    data.getLaureatImageBase64()+"",
                    data.getLaureatGender()+"",
                    data.getLaureatPromotion()+"",
                    data.getLaureatFiliere()+"",
                    data.getLaureatOrganisation()+"")));
        } else {
            registerResult.setValue(new RegisterResult(R.string.login_failed));
        }
    }

    void loginDataChanged(String username, String password, String Nom, String Prenom, String numero_tel,
                          String img_base64,
                          String LaureatGender,
                          String LaureatPromotion,
                          String LaureatFiliere,
                          String LaureatOrg) {
        if (!isUserNameValid(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null,null,null,null,null,null,null,null,null));
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_password, null,null,null,null,null,null,null,null));
        }
        else if (!isNomValid(Nom)) {
            registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_Nom,null,null,null,null,null,null,null));
        }

        else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        String expression = "([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9._-]+)$";
            String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                            +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                            +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                            +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
            Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(username);
        //return matcher.matches();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
    // A placeholder password validation check
    private boolean isNomValid(String Nom) {
        return Nom != null && Nom.trim().length() > 5;
    }
}
