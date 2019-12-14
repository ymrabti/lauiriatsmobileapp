package com.example.annuairelauriats.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.register.RegisterActivity;

import static com.example.annuairelauriats.ui.home.Classtest.getPref;
import static com.example.annuairelauriats.ui.home.Classtest.id_connected;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
private static Context context ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        int user = getPref(this);
        if (user!=-1){
            id_connected = user;
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);}

        final EditText usernameEditText = findViewById(R.id.username);usernameEditText.setText("younesmrabti50@gmail.com");
        final EditText passwordEditText = findViewById(R.id.password);passwordEditText.setText("jfhdhhdhhdxbjj");
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final TextView go_to_register = findViewById(R.id.go_to_register);
        go_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
        context=this;

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.isDataValid()){loginButton.setBackgroundResource(R.drawable.lo_gin);}
                if (!loginFormState.isDataValid()){loginButton.setBackgroundResource(R.drawable.lo_gin_disabled);}
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(Html.fromHtml("<font color='red'>"+getString(loginFormState.getUsernameError())+"</font>"));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(Html.fromHtml("<font color='red'>"+getString(loginFormState.getPasswordError())+"</font>"));
                }
            }
        });
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountDownTimer c = new CountDownTimer(500,50) {
                    @Override public void onTick(long millisUntilFinished) {
                        loadingProgressBar.setVisibility(View.VISIBLE);}
                    @Override public void onFinish() {
                        loginViewModel.login(usernameEditText.getText().toString(),passwordEditText.getText().toString());
                        loadingProgressBar.setVisibility(View.GONE);
                    }
                };c.start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int user = getPref(this);
        if (user!=-1){
            id_connected = user;
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);}
    }

    public static Context getContexte() {
        return context;
    }
}
