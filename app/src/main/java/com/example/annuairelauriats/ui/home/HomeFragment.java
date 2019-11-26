package com.example.annuairelauriats.ui.home;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.annuairelauriats.R;

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
                    result_http_client.setText(""
                            //+ Classtest.getJsonObjectById(getActivity(),"myjson.json",1).toString()
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }
}