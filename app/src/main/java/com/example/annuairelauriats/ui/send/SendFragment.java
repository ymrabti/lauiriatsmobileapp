package com.example.annuairelauriats.ui.send;

import android.os.Bundle;
import androidx.fragment.app.Fragment;


import static com.example.annuairelauriats.ui.home.Classtest.logout;

public class SendFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);logout(getContext());
    }
}