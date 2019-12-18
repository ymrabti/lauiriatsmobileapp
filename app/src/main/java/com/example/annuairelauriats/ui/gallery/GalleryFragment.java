package com.example.annuairelauriats.ui.gallery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.aide.HelpFragment;
import com.example.annuairelauriats.ui.slideshow.SlideshowFragment;
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
    private boolean isFABOpen;
    private FloatingActionButton fab;
    private LinearLayout fm1,fm2; private TextView t1,t2;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        malist = root.findViewById(R.id.list_laureat);

        long filiere = get_filter_pref_long(Objects.requireNonNull(getActivity()), "branch");
        String promo = get_filter_pref_string(getActivity(), "promotion");
        String secteur ;long org;
        try {
            assert getArguments() != null;
            org =getArguments().getLong("organisation");secteur="TOUT";
            JSONObject jsonObject= getJsonObjectBycle(getActivity(),"id",org,organismes);
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar())
                    .setTitle(jsonObject.getString("org"));
        }
        catch(Exception e){
            org = get_filter_pref_long(getActivity(), "organisation");
            secteur= get_filter_pref_string(getActivity(), "sector");
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar())
                    .setTitle("Liste des Laureats");
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
                 fab.animate().rotation(0);closeFABMenu();
            }
        });
        isFABOpen=false;
        FloatingActionButton fab1 = root.findViewById(R.id.go_to_slideshow_gallery);
        fm1=root.findViewById(R.id.afficher_dans_liste_ll_gallery);
        fm2=root.findViewById(R.id.appliquer_nouveau_filtre_ll_gallery);
        t1=root.findViewById(R.id.afficher_dans_liste_gallery);
        t2=root.findViewById(R.id.appliquer_nouveau_filtre_gallery);
        fab =  root.findViewById(R.id.fab_filter_choice_gallery);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    fab.animate().rotation(45);
                    showFABMenu();
                }else{
                    fab.animate().rotation(0);
                    closeFABMenu();
                }
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new HelpFragment());
                fragmentTransaction.replace(R.id.nav_host_fragment, new SlideshowFragment());
                fragmentTransaction.commit();
                MainActivity.navigationView.setCheckedItem(R.id.nav_slideshow);
            }
        });
        return root;
    }

    private void showFABMenu(){
        isFABOpen=true;
        fm1.animate().translationY(-(fab.getHeight()+10));
        fm2.animate().translationY(-2*(fab.getHeight()+10));
        t1.setVisibility(View.VISIBLE);t2.setVisibility(View.VISIBLE);
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fm1.animate().translationY(0);
        fm2.animate().translationY(0);
        t1.setVisibility(View.GONE);t2.setVisibility(View.GONE);
    }
}
