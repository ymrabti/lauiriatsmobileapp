package com.example.annuairelauriats.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.annuairelauriats.R;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.example.annuairelauriats.ui.home.Classtest.ip_server;
import static com.example.annuairelauriats.ui.home.Classtest.set0Pref;
import static com.example.annuairelauriats.ui.home.Classtest.email_connected;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    LiveData<LoginFormState> getLoginFormState() { return loginFormState; }
    public void login(final String username, final String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.getContexte());
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length()==0){
                    loginFormState.setValue(new LoginFormState(R.string.invalid_doesnotexist, null));
                }
                else{ authen(username,password);}
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, ip_server + "/laureat/email/"+username,
                null, listener, errorListener);
        requestQueue.add(jsonArrayRequest);
        /*try {
            if (!is_email_exist(LoginActivity.getContexte(),username)){
                loginFormState.setValue(new LoginFormState(R.string.invalid_doesnotexist, null));
            }
            else {
                if (!is_password_correct(LoginActivity.getContexte(),username,password)){
                    loginFormState.setValue(new LoginFormState(null, R.string.wrong_password));
                }
                else{
                    id_connected = getJsonObjectBykey(LoginActivity.getContexte(),"email",username,laureats).getInt("id");
                    setPref(LoginActivity.getContexte(),id_connected);
                    Intent i = new Intent(LoginActivity.getContexte(), MainActivity.class);
                    LoginActivity.getContexte().startActivity(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
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
    private void authen(final String email, String password){
        try{
            JSONObject passmail = new JSONObject();
            passmail.put("email",email);passmail.put("password",password);
            JSONArray jsonArray = new JSONArray();jsonArray.put(passmail);
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.getContexte());
            Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (response.length()==0){
                        loginFormState.setValue(new LoginFormState(null, R.string.wrong_password));
                    }
                    else{
                        //email_connected = email;
                        //set0Pref(LoginActivity.getContexte(),email);
                        Toast.makeText(LoginActivity.getContexte(),"success!!",Toast.LENGTH_LONG).show();
                    }
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            };
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.POST, ip_server + "/laureat/authentifie",
                    jsonArray, listener, errorListener);
            requestQueue.add(jsonArrayRequest);

        }catch (Exception e){}
    }
}
