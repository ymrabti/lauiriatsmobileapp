package com.example.annuairelauriats.ui.signaler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import com.example.annuairelauriats.R;

import java.util.Objects;

public class SignalerFragment extends Fragment {

    //private SignalerViewModel signalerViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //signalerViewModel = ViewModelProviders.of(this).get(SignalerViewModel.class);
        View root = inflater.inflate(R.layout.signalerproblem, container, false);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar())
                .setTitle("Modifier Vos Informations");
        return root;
    }
}
