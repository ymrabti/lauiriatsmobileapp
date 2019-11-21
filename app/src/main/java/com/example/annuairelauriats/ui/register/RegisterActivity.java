package com.example.annuairelauriats.ui.register;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.SettingsActivity;
import com.example.annuairelauriats.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel registerViewModel;
    private RadioGroup radioOrgGroup;
    private RadioButton radioButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerViewModel = ViewModelProviders.of(this, new RegisterViewModelFactory()).get(RegisterViewModel.class);

        final EditText usernameEditText = findViewById(R.id.usernamename);
        final EditText passwordEditText = findViewById(R.id.passwordword);
        final Button loginButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.registering);
        final TextView go_to_login = findViewById(R.id.go_to_login);
        go_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        registerViewModel.getREgisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        registerViewModel.getRegistreResult().observe(this, new Observer<RegisterResult>() {
            @Override
            public void onChanged(@Nullable RegisterResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.loginDataChanged(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerViewModel.register(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                registerViewModel.register(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
    radioOrgGroup = findViewById(R.id.radio_organisation);
    radioOrgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            radioButton = findViewById(checkedId);
            LinearLayout linearLayout;LinearLayout linearLayoutChecked;
            if (radioButton.getText().toString().contains("Organisation")){
                linearLayout  = findViewById(R.id.org_no_connue);linearLayout.setVisibility(View.GONE);
                linearLayoutChecked  = findViewById(R.id.org_connue);linearLayout.setVisibility(View.VISIBLE);
            }
            else{
                linearLayout  = findViewById(R.id.org_connue);linearLayout.setVisibility(View.GONE);
                linearLayoutChecked  = findViewById(R.id.org_no_connue);linearLayout.setVisibility(View.VISIBLE);
            }
        }
    });
    }

    private void updateUiWithUser(RegisterUserView model) {
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(),model.getDisplayName() , Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);startActivity(i);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), SettingsActivity.class);startActivity(i);
    }
}
