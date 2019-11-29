package com.example.annuairelauriats.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.home.Classtest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
public class GalleryFragment extends Fragment{
    private ListView malist;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        malist = root.findViewById(R.id.list_laureat);
        Classtest.peupler_array_list(getActivity(),1,"TOUT","TOUT",malist);
        malist.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) { }}
        );
        FloatingActionButton filter_fab = root.findViewById(R.id.fab_filter_laureat);
        filter_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Classtest.ShowPopupfilter(getActivity(),malist,null,1);
            }
        });
        return root;
    }

}
