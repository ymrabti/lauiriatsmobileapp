package com.example.annuairelauriats.ui.register;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.annuairelauriats.ui.home.Classtest.connect_to_backend;
import static com.example.annuairelauriats.ui.home.Classtest.connect_to_backend_array;
import static com.example.annuairelauriats.ui.home.Classtest.ip_server;
import static com.example.annuairelauriats.ui.home.Classtest.set0Pref;
import static com.example.annuairelauriats.ui.home.Classtest.email_connected;
import static com.example.annuairelauriats.ui.register.RegisterActivity.lat;
import static com.example.annuairelauriats.ui.register.RegisterActivity.lon;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    LiveData<RegisterFormState> getREgisterFormState() {
        return registerFormState;
    }
    public void register(
            final String emailUser, final String Password, final String LaureatNom, final String LaureatPrenom, final String LaureatNumTel,
            final String LaureatImageBase64, final String LaureatGender, final String LaureatPromotion, final long LaureatFiliere,
            final long org_selected, final String nomOrgEdited, final String secteurOrgEdited, final String initulePost,
            final String date_debut_travail_chez_org_, final String description, final long radio) {
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length()!=0){
                    registerFormState.setValue(new RegisterFormState(
                            null,
                            null, null,R.string.email_taken,
                            null,null,null,null,
                            null,null,null,null,null,
                            null,null,null,null));
                    Toast.makeText(RegisterActivity.getContextext(),R.string.email_taken,Toast.LENGTH_LONG).show();
                }
                else{
                    try{
                        Calendar rightNow = Calendar.getInstance();
                        int seconde = rightNow.get(Calendar.SECOND);int minute = rightNow.get(Calendar.MINUTE);
                        int heur = rightNow.get(Calendar.HOUR_OF_DAY);
                        int jour = rightNow.get(Calendar.DAY_OF_MONTH);int mois = rightNow.get(Calendar.MONTH)+1;
                        int annee = rightNow.get(Calendar.YEAR);
                        String dateNow = annee+"-"+mois+"-"+jour+" "+heur+":"+minute+":"+seconde;
                        JSONObject laureat_nouveau = new JSONObject();
                        laureat_nouveau.put("Nom",LaureatNom);laureat_nouveau.put("Prenom",LaureatPrenom);
                        laureat_nouveau.put("Gender",LaureatGender);laureat_nouveau.put("Promotion",LaureatPromotion);
                        laureat_nouveau.put("Filiere",LaureatFiliere);laureat_nouveau.put("DATE_INSCRIPTION",dateNow);
                        laureat_nouveau.put("Description",description);laureat_nouveau.put("Telephone",LaureatNumTel);
                        laureat_nouveau.put("email",emailUser);laureat_nouveau.put("Pass_word",Password);
                        laureat_nouveau.put("photo",LaureatImageBase64);
                        JSONObject statut_laureat = new JSONObject();
                        statut_laureat.put("id_statut",1);
                        statut_laureat.put("id_laureat",emailUser);
                        statut_laureat.put("motif","nouveau");

                        JSONObject new_org_attentente = new JSONObject();
                        new_org_attentente.put("Nom",nomOrgEdited);
                        new_org_attentente.put("Latitude",lat);
                        new_org_attentente.put("Longitude",lon);
                        new_org_attentente.put("laureat",emailUser);
                        new_org_attentente.put("intitule",initulePost);
                        new_org_attentente.put("secteur",secteurOrgEdited);
                        new_org_attentente.put("date_debut",date_debut_travail_chez_org_);


                        JSONObject org_laureat_new = new JSONObject();
                        org_laureat_new.put("org",org_selected);org_laureat_new.put("laureat",emailUser);
                        org_laureat_new.put("date_debut",date_debut_travail_chez_org_);
                        org_laureat_new.put("fonction",initulePost);

                        if (radio==0){
                            laureat_nouveau.put("org",org_selected);
                            signup(emailUser,laureat_nouveau,statut_laureat,org_laureat_new,null);
                        }
                        else{
                            laureat_nouveau.put("org",0);
                            signup(emailUser,laureat_nouveau,statut_laureat,null,new_org_attentente);
                        }

                    }
                    catch(Exception e){
                        Toast.makeText(RegisterActivity.getContextext(),"hna  hh ",Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };
        connect_to_backend_array(RegisterActivity.getContextext(), Request.Method.GET,"/laureat/email/"+emailUser,null
                ,listener,errorListener);
    }

    private void signup(final String emailUser, JSONObject laureat_nouveau,JSONObject statut_laureat
            ,JSONObject org_laureat_new,JSONObject new_org_attentente){

        if(org_laureat_new==null){
            connect_to_backend(RegisterActivity.getContextext(),Request.Method.POST, "/autres/insert_org_attente", new_org_attentente
                    , new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    },null);
        }
        else if (new_org_attentente==null){
            connect_to_backend(RegisterActivity.getContextext(),Request.Method.POST,"/laureat/new_row_org_laureat",org_laureat_new
                    ,new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    }, null);
        }

        connect_to_backend(RegisterActivity.getContextext(),Request.Method.POST, "/laureat/new_row_statut_laureat", statut_laureat
                , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },null);

        connect_to_backend(RegisterActivity.getContextext(),Request.Method.POST,"/laureat", laureat_nouveau,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        email_connected = emailUser;
                        set0Pref(RegisterActivity.getContextext(), emailUser);
                        Toast.makeText(RegisterActivity.getContextext(), "success!!", Toast.LENGTH_LONG).show();
                        RegisterActivity.getContextext().startActivity(
                                new Intent(RegisterActivity.getContextext()
                                        , MainActivity.class));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

    }

    void loginDataChanged(
            String LaureatNom,String LaureatPrenom,  String LaureatNumTel,String emailUser, String Password,
            String LaureatImageBase64, String LaureatGender, String LaureatPromotion, long LaureatFiliere,
            String initulePost,
            String date_debut_travail_chez_org_,String description) {
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
        else if (!isSelectDropDownValid(LaureatGender)) {
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
    private boolean isEmailValid(String username) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }
    private boolean isLegalDate(String s) {
        Matcher m = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}$", Pattern.CASE_INSENSITIVE).matcher(s);
        return m.matches();
    }
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
    static boolean isNomValid(String Nom) {
        return Nom != null && Nom.trim().length() > 3;
    }
    private boolean isNumtelValid(String tel){
        Matcher m = Pattern.compile("\\d{5,16}$", Pattern.CASE_INSENSITIVE).matcher(tel);
        return m.matches();
    }
    static boolean isSelectDropDownValid(long selcected_id){
        return selcected_id!=0;
    }
    static boolean isSelectDropDownValid(String selcected){
        return !selcected.equals("SELECTIONNER");
    }

}
