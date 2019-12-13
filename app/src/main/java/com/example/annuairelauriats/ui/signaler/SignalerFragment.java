package com.example.annuairelauriats.ui.signaler;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.annuairelauriats.R;

public class SignalerFragment extends Fragment {

    private SignalerViewModel signalerViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        signalerViewModel = ViewModelProviders.of(this).get(SignalerViewModel.class);
        View root = inflater.inflate(R.layout.signalerproblem, container, false);

        return root;
    }
}
