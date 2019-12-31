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
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.aide.HelpFragment;
import com.example.annuairelauriats.ui.slideshow.SlideshowFragment;
import com.example.annuairelauriats.ui.tools.ToolsFragment;

import java.util.ArrayList;
import java.util.Objects;
import static com.example.annuairelauriats.ui.home.Classtest.ShowPopupfilter;
import static com.example.annuairelauriats.ui.home.Classtest.get_filter_pref_long;
import static com.example.annuairelauriats.ui.home.Classtest.get_filter_pref_string;
import static com.example.annuairelauriats.ui.home.Classtest.show_laureats_on_list;
import static com.example.annuairelauriats.ui.home.Classtest.sql;

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

        show_laureats_on_list(getActivity(),sql,malist);
        malist.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Laureat laureat = laureats_list.get(position);
                        //id_selected = laureat.getId();
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
