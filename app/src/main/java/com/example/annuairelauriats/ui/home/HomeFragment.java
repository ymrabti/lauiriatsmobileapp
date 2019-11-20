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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
                /*HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(url);
                HttpResponse response;
                try {
                    response = httpClient.execute(httpget);
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream instream = entity.getContent();
                        String result= convertStreamToString(instream);
                        result_http_client.setText(result);
                        instream.close();
                    }

                } catch (Exception e) {
                    result_http_client.append(e+" :    "+url+" \n");
                }*/

            }
        });
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}