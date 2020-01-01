package com.example.annuairelauriats.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.aide.HelpFragment;
import com.example.annuairelauriats.ui.slideshow.SlideshowFragment;
import com.example.annuairelauriats.ui.tools.ToolsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.annuairelauriats.ui.home.Classtest.ShowPopupfilter;
import static com.example.annuairelauriats.ui.home.Classtest.additional_sql;
import static com.example.annuairelauriats.ui.home.Classtest.connect_to_backend_array;
import static com.example.annuairelauriats.ui.home.Classtest.email_selected;
import static com.example.annuairelauriats.ui.home.Classtest.setclipboard;
import static com.example.annuairelauriats.ui.home.Classtest.shared_org;
import static com.example.annuairelauriats.ui.home.Classtest.show_laureats_on_list;
import static com.example.annuairelauriats.ui.home.Classtest.sql;

public class GalleryFragment extends Fragment{
    static private ListView malist;public static ArrayList<Laureat> laureats_list;
    private static Context context;
    private static FragmentTransaction fragmentTransaction;
    public static ImageView imageView_nodata;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        malist = root.findViewById(R.id.list_laureat);context=getActivity();
        setHasOptionsMenu(true);
        assert getFragmentManager() != null;
        fragmentTransaction = getFragmentManager().beginTransaction();
        imageView_nodata= root.findViewById(R.id.image_no_data);


        try {
            if (getArguments().getLong("mark")==0){
                long org= getArguments().getLong("organisation");
                show_laureats_on_list(getActivity(),sql+additional_sql+" and org = "+org,malist);
                Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject laureat = response.getJSONObject(0);
                            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar())
                                    .setTitle(laureat.getString("Nom"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"org  "+e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                };
                connect_to_backend_array(getContext(), Request.Method.GET,  "/laureat/org/"+org,
                        null, listener, errorListener);
            }
            if (getArguments().getLong("mark")==1){
                if (shared_org!=0){additional_sql=additional_sql+" and org = "+shared_org;}
                show_laureats_on_list(getActivity(),sql+additional_sql,malist);
            }
        }
        catch (Exception e){
            show_laureats_on_list(getActivity(),sql,malist);
        }


        malist.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Laureat laureat = laureats_list.get(position);
                        email_selected = laureat.getId();
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
        Fragment fragment = new SlideshowFragment();
        Bundle bundle=new Bundle();
        bundle.putLong("mark", 1);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
        MainActivity.navigationView.setCheckedItem(R.id.nav_slideshow);
    }
}
