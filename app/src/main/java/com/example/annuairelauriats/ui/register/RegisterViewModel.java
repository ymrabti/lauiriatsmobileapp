package com.example.annuairelauriats.ui.register;

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
            String emailUser, String Password, String LaureatNom, String LaureatPrenom, String LaureatNumTel,
            String LaureatImageBase64, String LaureatGender, String LaureatPromotion, long LaureatFiliere,
            long org_selected, String nomOrgEdited, String secteurOrgEdited,String initulePost,
            String date_debut_travail_chez_org_,String description) {
        // can be launched in a separate asynchronous job
        Result<RegistredUser> result = registerRepository.Register(
                emailUser, Password, LaureatNom, LaureatPrenom, LaureatNumTel,
                LaureatImageBase64, LaureatGender, LaureatPromotion, LaureatFiliere,
        org_selected, nomOrgEdited, secteurOrgEdited,initulePost,
                date_debut_travail_chez_org_,description);

        if (result instanceof Result.Success) {
            RegistredUser data = ((Result.Success<RegistredUser>) result).getData();
            registerResult.setValue(new RegisterResult(new RegisterUserView(
                    data.getEmailUser(),
                    data.getPassWordUser(),
                    data.getLaureatNom(),
                    data.getLaureatPrenom(),
                    data.getLaureatNumTel(),
                    data.getLaureatImageBase64(),
                    data.getLaureatGender(),
                    data.getLaureatPromotion(),
                    data.getLaureatFiliere(),
                    data.getOrg_selected(),data.getNomOrgEdited(),
                    data.getSecteurOrgEdited(),data.getInitulePost(),data.getDate_debut_travail_chez_org(),data.getDescription())));
        } else {
            registerResult.setValue(new RegisterResult(R.string.login_failed));
        }
    }

    void loginDataChanged(
            String LaureatNom,String LaureatPrenom,  String LaureatNumTel,String emailUser, String Password,
            String LaureatImageBase64, String LaureatGender, String LaureatPromotion, long LaureatFiliere,
            long org_selected, String nomOrgEdited, String secteurOrgEdited,String initulePost,
            String date_debut_travail_chez_org_,String description,long radio) {
        if (!isNomValid(LaureatNom)) {
            registerFormState.setValue(new RegisterFormState(
                    R.string.invalid_Nom,
                    null, null,null,
                    null,null,null,null,
                    null,null,null,null,null,
                    null,null,null,null));

        }
        else if (!isNomValid(LaureatPrenom)) {
            registerFormState.setValue(new RegisterFormState(
                    null,
                    R.string.invalid_Nom, null,null,
                    null,null,null,null,
                    null,null,null,null,null,
                    null,null,null,null));

        }
        else if (!isGenderValid(LaureatGender)) {
            registerFormState.setValue(new RegisterFormState(
                    null,
                    null, null,null,
                    null,null,null,null,
                    null,null,null,null,R.string.invalid_gender,
                    null,null,null,null));
        }
        else if (!isNumtelValid(LaureatNumTel)) {
            registerFormState.setValue(new RegisterFormState(
                    null,
                    null, R.string.invalid_Tel,null,
                    null,null,null,null,
                    null,null,null,null,null,
                    null,null,null,null));

        }
        else if (!isSelectDropDownValid(LaureatFiliere)) {
            registerFormState.setValue(new RegisterFormState(
                    null,
                    null, null,null,
                    null,null,null,null,
                    null,null,null,null,null,
                    R.string.invalid_filier,null,null,null));
        }
        else if (!isSelectDropDownValid(LaureatPromotion)) {
            registerFormState.setValue(new RegisterFormState(
                    null,
                    null, null,null,
                    null,null,null,null,
                    null,null,null,null,null,
                    null,R.string.invalid_promotion,null,null));
        }
        else if (LaureatImageBase64.isEmpty()) {
            registerFormState.setValue(new RegisterFormState(
                    null,
                    null, null,null,
                    R.string.invalid_image,null,null,null,
                    null,null,null,null,null,
                    null,null,null,null));
        }
        else if (!isEmailValid(emailUser)) {
            registerFormState.setValue(new RegisterFormState(
                    null,
                    null, null,R.string.invalid_username,
                    null,null,null,null,
                    null,null,null,null,null,
                    null,null,null,null));
        }
        else if (!isPasswordValid(Password)) {
            registerFormState.setValue(new RegisterFormState(
                    null,
                    null, null,null,
                    null,R.string.invalid_password,null,null,
                    null,null,null,null,null,
                    null,null,null,null));
        }
        else if (radio==0 ) {
            if (!isSelectDropDownValid(org_selected)){
                registerFormState.setValue(new RegisterFormState(
                        null,
                        null, null,null,
                        null,null,null,null,
                        null,null,null,null,null,
                        null,null,R.string.invalid_org,null));}
        }
        else if (radio==1 ) {
            if (!isNomValid(nomOrgEdited)){
                registerFormState.setValue(new RegisterFormState(
                        null,
                        null, null,null,
                        null,null,null,R.string.invalid_Nom,
                        null,null,null,null,null,
                        null,null,null,null));}
            else if (!isSelectDropDownValid(secteurOrgEdited)){
                registerFormState.setValue(new RegisterFormState(
                        null,
                        null, null,null,
                        null,null,null,null,
                        null,null,null,null,null,
                        null,null,null,R.string.invalid_secteur));}
        }
        else if (!isLegalDate(date_debut_travail_chez_org_)) {
            registerFormState.setValue(new RegisterFormState(
                    null,
                    null, null,null,
                    null,null,null,null,
                    null,R.string.invalid_date,null,null,null,
                    null,null,null,null));
        }
        else if (!isNomValid(initulePost)) {
            registerFormState.setValue(new RegisterFormState(
                    null,
                    null, null,null,
                    null,null,null,null,
                    null,null,R.string.invalid_Nom,null,null,
                    null,null,null,null));
        }
        else if (!isNomValid(description)) {
            registerFormState.setValue(new RegisterFormState(
                    null,
                    null, null,null,
                    null,null,null,null,
                    null,null,null,R.string.invalid_Nom,null,
                    null,null,null,null));
        }
        else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isEmailValid(String username) {
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
    boolean isLegalDate(String s) {
        Matcher m = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}$", Pattern.CASE_INSENSITIVE).matcher(s);
        return m.matches();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
    // A placeholder password validation check
    private boolean isNomValid(String Nom) {
        return Nom != null && Nom.trim().length() > 3;
    }
    private boolean isNumtelValid(String tel){
        Matcher m = Pattern.compile("\\d{5,16}$", Pattern.CASE_INSENSITIVE).matcher(tel);
        return m.matches();
    }

    private boolean isGenderValid(String gender){
        return (gender.equals("M")||gender.equals("F"));
    }

    private boolean isSelectDropDownValid(long selcected_id){
        return selcected_id!=0;
    }
    private boolean isSelectDropDownValid(String selcected){
        return !selcected.equals("SELECTIONNER");
    }
    private boolean is_radio_valid(long radio){
        return radio==0||radio==1;
    }

}
