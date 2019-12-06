package com.example.annuairelauriats.ui.send;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.example.annuairelauriats.ui.login.LoginActivity;

import java.util.Objects;

import static com.example.annuairelauriats.ui.home.Classtest.setPref;

public class SendFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);logout();
    }
    private void logout(){
        setPref(Objects.requireNonNull(getContext()),-1);
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }
}