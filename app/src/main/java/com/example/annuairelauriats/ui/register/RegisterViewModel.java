package com.example.annuairelauriats.ui.register;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.home.Classtest;
import com.example.annuairelauriats.ui.login.LoginActivity;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.example.annuairelauriats.ui.home.Classtest.getLastID;
import static com.example.annuairelauriats.ui.home.Classtest.id_connected;
import static com.example.annuairelauriats.ui.home.Classtest.is_email_exist;
import static com.example.annuairelauriats.ui.home.Classtest.new_Laureat_Register;
import static com.example.annuairelauriats.ui.home.Classtest.new_org_attente_admin;
import static com.example.annuairelauriats.ui.home.Classtest.setNewImgLaureat;
import static com.example.annuairelauriats.ui.home.Classtest.setPref;
import static com.example.annuairelauriats.ui.register.RegisterActivity.lat;
import static com.example.annuairelauriats.ui.register.RegisterActivity.lon;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    LiveData<RegisterFormState> getREgisterFormState() {
        return registerFormState;
    }
    public void register(
            String emailUser, String Password, String LaureatNom, String LaureatPrenom, String LaureatNumTel,
            String LaureatImageBase64, String LaureatGender, String LaureatPromotion, long LaureatFiliere,
            long org_selected, String nomOrgEdited, String secteurOrgEdited,String initulePost,
            String date_debut_travail_chez_org_,String description) {
        try {
            if (!is_email_exist(RegisterActivity.getContextext(),emailUser)){
                Calendar rightNow = Calendar.getInstance();
                int seconde = rightNow.get(Calendar.SECOND);int minute = rightNow.get(Calendar.MINUTE);
                int heur = rightNow.get(Calendar.HOUR_OF_DAY);
                int jour = rightNow.get(Calendar.DAY_OF_MONTH);int mois = rightNow.get(Calendar.MONTH)+1;
                int annee = rightNow.get(Calendar.YEAR);
                String dateNow = annee+"-"+mois+"-"+jour+" "+heur+":"+minute+":"+seconde;
                int id_laureat_actuelle = getLastID(RegisterActivity.getContextext(),Classtest.laureats);
                id_connected = id_laureat_actuelle;
                new_Laureat_Register(
                        RegisterActivity.getContextext(),id_laureat_actuelle,
                        LaureatNom+"", LaureatPrenom +"", LaureatGender+"",
                        LaureatPromotion+"", LaureatFiliere, emailUser+"",
                        Password+"", LaureatNumTel+"", dateNow+"",
                        description+"");
                setNewImgLaureat(RegisterActivity.getContextext(),LaureatImageBase64,id_laureat_actuelle);
                if(!nomOrgEdited.isEmpty()){
                    new_org_attente_admin(RegisterActivity.getContextext(),nomOrgEdited+"",id_laureat_actuelle,
                            lat,lon,secteurOrgEdited,
                            date_debut_travail_chez_org_+"",initulePost+"");
                }
                else{
                    Classtest.new_org_laureat(RegisterActivity.getContextext(),org_selected,id_laureat_actuelle,
                            date_debut_travail_chez_org_,initulePost+"");
                }
                setPref(LoginActivity.getContexte(),id_connected);
                RegisterActivity.getContextext().startActivity(new Intent(RegisterActivity.getContextext(), MainActivity.class));

            }
            else{
                registerFormState.setValue(new RegisterFormState(
                        null,
                        null, null,R.string.email_taken,
                        null,null,null,null,
                        null,null,null,null,null,
                        null,null,null,null));
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
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

}
