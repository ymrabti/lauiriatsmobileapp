package com.example.annuairelauriats.ui.standards;

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

public class StandardsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StandardsViewModel standardsViewModel;
        standardsViewModel = ViewModelProviders.of(this).get(StandardsViewModel.class);
        View root = inflater.inflate(R.layout.standardscommunute, container, false);
        final TextView textView = root.findViewById(R.id.text_standardscommunute);
        standardsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
