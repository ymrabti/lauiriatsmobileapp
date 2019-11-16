package com.example.annuairelauriats.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.annuairelauriats.R;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HomeFragment extends Fragment  {
    private EditText result_http_client;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel;
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        String url = "http://192.168.137.1:8090/laureat_api/Laureat/";
        final EditText URL_TextEdit = root.findViewById(R.id.editText1);URL_TextEdit.append(url);
        result_http_client=root.findViewById(R.id.result_http_client);
        Button but_connect = root.findViewById(R.id.buttonSend);
        but_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { String ok = URL_TextEdit.getText().toString();
                result_http_client.setText("");
                connecting_rest(ok); }});
        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    public void connecting_rest(final String url) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                URL laureatEndpoint;
                try {
                    laureatEndpoint = new URL(url);
                    result_http_client.append(url+"\n");
                    HttpURLConnection myConnection = (HttpURLConnection) laureatEndpoint.openConnection();
                    myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");
                    result_http_client.append("RESPONSE CODE  :  "+myConnection.getResponseCode()+" \n\n");
                    if (myConnection.getResponseCode() == 200) {
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                        DataInputStream in = new DataInputStream(responseBody);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String strLine;
                        while ((strLine = br.readLine()) != null) {
                            result_http_client.append(strLine+"\n");
                        }
                        in.close();br.close();responseBody.close();
                    }
                    myConnection.disconnect();
                } catch (Exception e) {
                    result_http_client.append(e+" \nexception\n"+" \n\n");
                }
            }
        });
    }
}