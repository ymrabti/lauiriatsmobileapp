package com.example.annuairelauriats.ui.signaler;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.register.RegisterActivity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.annuairelauriats.ui.signaler.SignalerFragment.statut;

public class SignalerViewModel extends ViewModel {

    private MutableLiveData<SignalerFormState> signalerFormState = new MutableLiveData<>();

    public MutableLiveData<SignalerFormState> getSignalerFormState() {
        return signalerFormState;
    }
    public void update_data(
            String emailUser, String Password, String LaureatNom, String LaureatPrenom, String LaureatNumTel,
            String LaureatImageBase64, String LaureatGender, String LaureatPromotion, long LaureatFiliere,
            long org_selected, String nomOrgEdited, String secteurOrgEdited,String initulePost,
            String date_debut_travail_chez_org_,String description,long radio,double lat,double lon) {
        try {
            if (radio==2131296409 && !isSelectDropDownValid(org_selected)) {
                signalerFormState.setValue(new SignalerFormState(
                        null,
                        null, null,null,
                        null,null,null,null,
                        null,null,null,null,null,
                        null,null,R.string.invalid_org,null));
            }
            else if (radio==2131296410 && !isNomValid(nomOrgEdited)) {
                signalerFormState.setValue(new SignalerFormState(
                        null,
                        null, null,null,
                        null,null,null,R.string.invalid_Nom,
                        null,null,null,null,null,
                        null,null,null,null));
            }
            else if (radio==2131296410 && !isSelectDropDownValid(secteurOrgEdited)) {
                signalerFormState.setValue(new SignalerFormState(
                        null,
                        null, null,null,
                        null,null,null,null,
                        null,null,null,null,null,
                        null,null,null,R.string.invalid_secteur));
            }
            else if (radio==2131296410 && (lat==0 ||lon==0)) {
                signalerFormState.setValue(new SignalerFormState(
                        null,
                        null, null,null,
                        null,null,R.string.invalid_position,null,
                        null,null,null,null,null,
                        null,null,null,null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loginDataChanged(
            String LaureatNom,String LaureatPrenom,  String LaureatNumTel,String emailUser, String Password,
            String LaureatImageBase64,  String LaureatPromotion, long LaureatFiliere,
            String initulePost,
            String date_debut_travail_chez_org_,String description) {
        if (!isNomValid(LaureatNom) && statut!=4) {
            signalerFormState.setValue(new SignalerFormState(
                    R.string.invalid_Nom,
                    null, null,null,
                    null,null,null,null,
                    null,null,null,null,null,
                    null,null,null,null));

        }
        else if (!isNomValid(LaureatPrenom) && statut!=4) {
            signalerFormState.setValue(new SignalerFormState(
                    null,
                    R.string.invalid_Nom, null,null,
                    null,null,null,null,
                    null,null,null,null,null,
                    null,null,null,null));

        }
        else if (!isNumtelValid(LaureatNumTel)) {
            signalerFormState.setValue(new SignalerFormState(
                    null,
                    null, R.string.invalid_Tel,null,
                    null,null,null,null,
                    null,null,null,null,null,
                    null,null,null,null));

        }
        else if (!isSelectDropDownValid(LaureatFiliere) && statut!=4) {
            signalerFormState.setValue(new SignalerFormState(
                    null,
                    null, null,null,
                    null,null,null,null,
                    null,null,null,null,null,
                    R.string.invalid_filier,null,null,null));
        }
        else if (!isSelectDropDownValid(LaureatPromotion) && statut!=4) {
            signalerFormState.setValue(new SignalerFormState(
                    null,
                    null, null,null,
                    null,null,null,null,
                    null,null,null,null,null,
                    null,R.string.invalid_promotion,null,null));
        }
        else if (LaureatImageBase64.isEmpty()) {
            signalerFormState.setValue(new SignalerFormState(
                    null,
                    null, null,null,
                    R.string.invalid_image,null,null,null,
                    null,null,null,null,null,
                    null,null,null,null));
        }
        else if (!isEmailValid(emailUser)) {
            signalerFormState.setValue(new SignalerFormState(
                    null,
                    null, null,R.string.invalid_username,
                    null,null,null,null,
                    null,null,null,null,null,
                    null,null,null,null));
        }
        else if (!isPasswordValid(Password)) {
            signalerFormState.setValue(new SignalerFormState(
                    null,
                    null, null,null,
                    null,R.string.invalid_password,null,null,
                    null,null,null,null,null,
                    null,null,null,null));
        }
        else if (!isNomValid(description)) {
            signalerFormState.setValue(new SignalerFormState(
                    null,
                    null, null,null,
                    null,null,null,null,
                    null,null,null,R.string.invalid_Nom,null,
                    null,null,null,null));
        }
        else if (!isLegalDate(date_debut_travail_chez_org_)) {
            signalerFormState.setValue(new SignalerFormState(
                    null,
                    null, null,null,
                    null,null,null,null,
                    null,R.string.invalid_date,null,null,null,
                    null,null,null,null));
        }
        else if (!isNomValid(initulePost)) {
            signalerFormState.setValue(new SignalerFormState(
                    null,
                    null, null,null,
                    null,null,null,null,
                    null,null,R.string.invalid_Nom,null,null,
                    null,null,null,null));
        }
        else {
            signalerFormState.setValue(new SignalerFormState(true));
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
    private boolean isNumtelValid(String tel){
        Matcher m = Pattern.compile("\\d{5,16}$", Pattern.CASE_INSENSITIVE).matcher(tel);
        return m.matches();
    }


    private boolean isNomValid(String Nom) {
        return Nom != null && Nom.trim().length() > 3;
    }
    private boolean isSelectDropDownValid(long selcected_id){
        return selcected_id!=0;
    }
    private boolean isSelectDropDownValid(String selcected){
        return !selcected.equals("SELECTIONNER");
    }
}
