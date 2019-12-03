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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.tools.ToolsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.annuairelauriats.ui.home.Classtest.ShowPopupfilter;
import static com.example.annuairelauriats.ui.home.Classtest.loadJSONFromAsset;
import static com.example.annuairelauriats.ui.home.Classtest.peupler_array_list;
import static com.example.annuairelauriats.ui.home.Classtest.id_selected;
import static com.example.annuairelauriats.ui.home.Classtest.filter;

public class GalleryFragment extends Fragment{
    private ListView malist;public static ArrayList<Laureat> laureats_list;
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
        int filiere=0;String promo="TOUT";
        try {
            JSONObject filter_json = new JSONObject(
                    Objects.requireNonNull(loadJSONFromAsset(
                            Objects.requireNonNull(getActivity()), filter)));
            filiere=filter_json.getInt("filiere");promo=filter_json.getString("promotion");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        peupler_array_list(getActivity(),filiere,promo,"TOUT",malist);
        malist.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Laureat laureat = laureats_list.get(position);
                        id_selected = laureat.getId();
                        assert getFragmentManager() != null;
                        Fragment fragment = new ToolsFragment();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }}
        );
        FloatingActionButton filter_fab = root.findViewById(R.id.fab_filter_laureat);
        filter_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 ShowPopupfilter(getActivity(),malist,null,1);
            }
        });
        return root;
    }

}
