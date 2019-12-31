package com.example.annuairelauriats.ui.login;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.register.RegisterActivity;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.example.annuairelauriats.ui.home.Classtest.email_connected;
import static com.example.annuairelauriats.ui.home.Classtest.get0Pref;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private EditText usernameEditText,passwordEditText;private Button loginButton;
    public static ProgressBar loadingProgressBar;private TextView go_to_register;
private static Context context ;
    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        String user = get0Pref(this);
        if (!user.equals("noreply")){
            email_connected = user;
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);}

        usernameEditText = findViewById(R.id.username);usernameEditText.setText("younes.mrabti50@gmail.com");
        passwordEditText = findViewById(R.id.password);passwordEditText.setText("123123");
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        go_to_register = findViewById(R.id.go_to_register);
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
                loginViewModel.login(usernameEditText.getText().toString(),passwordEditText.getText().toString());
                loadingProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String user = get0Pref(this);
        if (!user.equals("noreply")){
            email_connected = user;
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);}
    }
    public static String getUniqueIMEIId(Context context) {
        try {

            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei;

            if (ContextCompat.checkSelfPermission(context
                    , android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imei =  telephonyManager.getImei();
                } else {
                    imei = telephonyManager.getDeviceId();
                }

                if (imei != null && !imei.isEmpty()) {
                    return imei;
                } else {
                    return android.os.Build.SERIAL;
                }
            }

        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            return e.toString();
//e.printStackTrace();
        }

        return "not_found";
    }
    public static Context getContexte() {
        return context;
    }
}
