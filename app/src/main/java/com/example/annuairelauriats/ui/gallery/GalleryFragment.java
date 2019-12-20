package com.example.annuairelauriats.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
    static private ListView malist;public static ArrayList<Laureat> laureats_list;
    private static Context context;
    private static FragmentTransaction fragmentTransaction;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        malist = root.findViewById(R.id.list_laureat);context=getActivity();
        setHasOptionsMenu(true);
        assert getFragmentManager() != null;
        fragmentTransaction = getFragmentManager().beginTransaction();

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
        return root;
    }
    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_filter_new).setVisible(true);
        menu.findItem(R.id.action_goto_map).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }
    public static void popup(){
        ShowPopupfilter(context,malist,null,1);
    }
    public static void to_map(){
        fragmentTransaction.replace(R.id.nav_host_fragment, new HelpFragment());
        fragmentTransaction.replace(R.id.nav_host_fragment, new SlideshowFragment());
        fragmentTransaction.commit();
        MainActivity.navigationView.setCheckedItem(R.id.nav_slideshow);
    }
}
