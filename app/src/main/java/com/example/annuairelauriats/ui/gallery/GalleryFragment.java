package com.example.annuairelauriats.ui.gallery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.tools.ToolsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import static com.example.annuairelauriats.ui.home.Classtest.ShowPopupfilter;
import static com.example.annuairelauriats.ui.home.Classtest.getJsonObjectBycle;
import static com.example.annuairelauriats.ui.home.Classtest.get_filter_pref_long;
import static com.example.annuairelauriats.ui.home.Classtest.get_filter_pref_string;
import static com.example.annuairelauriats.ui.home.Classtest.organismes;
import static com.example.annuairelauriats.ui.home.Classtest.peupler_array_list;
import static com.example.annuairelauriats.ui.home.Classtest.id_selected;

public class GalleryFragment extends Fragment{
    private ListView malist;public static ArrayList<Laureat> laureats_list;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        TextView textView = root.findViewById(R.id.text_gallery);
        malist = root.findViewById(R.id.list_laureat);

        long filiere = get_filter_pref_long(Objects.requireNonNull(getActivity()), "branch");
        String promo = get_filter_pref_string(getActivity(), "promotion");
        String secteur ;long org;
        try {
            assert getArguments() != null;
            org =getArguments().getLong("organisation");secteur="TOUT";
            JSONObject jsonObject= getJsonObjectBycle(getActivity(),"id",org,organismes);
            textView.setText(jsonObject.getString("org"));
        }
        catch(Exception e){
            org = get_filter_pref_long(getActivity(), "organisation");
            secteur= get_filter_pref_string(getActivity(), "sector");
            textView.setText("Liste des Laureats");
        }
        peupler_array_list(getActivity(), filiere, promo,"TOUT", org, secteur,malist);
        malist.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Laureat laureat = laureats_list.get(position);
                        id_selected = laureat.getId();
                        assert getFragmentManager() != null;
                        Fragment fragment = new ToolsFragment();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
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
