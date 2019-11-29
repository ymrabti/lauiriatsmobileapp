package com.example.annuairelauriats.ui.home;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.aide.HelpFragment;
import com.example.annuairelauriats.ui.send.SendFragment;
import com.example.annuairelauriats.ui.slideshow.SlideshowFragment;

import java.util.Objects;

public class HomeFragment extends Fragment  {
    private TextView result_http_client;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //HomeViewModel homeViewModel;homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        String url = "http://192.168.137.1:8090/laureat_api/Laureat/";
        final TextView URL_TextEdit = root.findViewById(R.id.editText1);URL_TextEdit.append(url);
        result_http_client=root.findViewById(R.id.result_http_client);
        Button but_connect = root.findViewById(R.id.buttonSend);
        but_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String string = "\n\n"+getActivity().getCacheDir()+
                            "\n\n"+getActivity().getFilesDir()+
                            "\n\n"+getActivity().getDatabasePath("database")+
                            "\n\n"+getActivity().getDataDir()+
                            "\n\n"+getActivity().getDir("dirc", Context.MODE_PRIVATE)+
                            "\n\n"+getActivity().getNoBackupFilesDir()+
                            "\n\n"+getActivity().getObbDir()
                            ;
                    result_http_client.setText(string);
                    /*String string = "{\n" +
                            "\"organisme\":1,\"province\":djnfjj,\"filiere\":3,\"promotion\":\"2020\"\n" +
                            "}";
                    Classtest.write_file_cache(getContext(),string);
                    result_http_client.setText(Classtest.loadJSONfromCACHE(getContext()));
                    assert getFragmentManager() != null;
                    Fragment fragment = new HelpFragment();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    MainActivity.navigationView.setCheckedItem(R.id.nav_slideshow);

                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.nav_host_fragment, new SlideshowFragment());
                    //MainActivity.navigationView.getCheckedItem();
                    result_http_client.setText(MainActivity.navigationView.getCheckedItem().getGroupId()+""*//*+ Classtest.getJsonObjectById(getActivity(),"myjson.json",1).toString()*//*);
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);
                    trans.commit();*/
                } catch (Exception e) {
                    e.printStackTrace();
                    result_http_client.setText(e.toString()/*+ Classtest.getJsonObjectById(getActivity(),"myjson.json",1).toString()*/);
                }
            }
        });
        return root;
    }
}