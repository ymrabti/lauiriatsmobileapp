package com.example.annuairelauriats.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.annuairelauriats.ui.home.Classtest.connect_to_backend;
import static com.example.annuairelauriats.ui.home.Classtest.connect_to_backend_array;
import static com.example.annuairelauriats.ui.home.Classtest.getUniqueIMEIId;
import static com.example.annuairelauriats.ui.home.Classtest.set0Pref;
import static com.example.annuairelauriats.ui.home.Classtest.email_connected;
import static com.example.annuairelauriats.ui.login.LoginActivity.loadingProgressBar;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    LiveData<LoginFormState> getLoginFormState() { return loginFormState; }
    public void login(final String username, final String password) {
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
        connect_to_backend_array(LoginActivity.getContexte(),Request.Method.GET,  "/laureat/email/"+username,
                null, listener, errorListener);
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

            Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (response.length()==0){
                        loginFormState.setValue(new LoginFormState(null, R.string.wrong_password));
                    }
                    else{
                        email_connected = email;
                        loadingProgressBar.setVisibility(View.GONE);
                        set0Pref(LoginActivity.getContexte(),email);
                        Toast.makeText(LoginActivity.getContexte(),"success!!",Toast.LENGTH_LONG).show();
                        LoginActivity.getContexte().startActivity(new Intent(LoginActivity.getContexte(), MainActivity.class));
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("laureat",email_connected);
                            jsonObject.put("IME",getUniqueIMEIId());
                            connect_to_backend(LoginActivity.getContexte(), Request.Method.POST
                                    , "/autres/insert_device", jsonObject, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            };
            Response.ErrorListener errorListener  = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.getContexte(),error.toString(),Toast.LENGTH_LONG).show();
                }
            };
            connect_to_backend_array(LoginActivity.getContexte(),Request.Method.POST, "/laureat/authentifie",
                    jsonArray, listener, errorListener);

        }
        catch (Exception e){Toast.makeText(LoginActivity.getContexte(),e.toString(),Toast.LENGTH_LONG).show();}
    }
}
