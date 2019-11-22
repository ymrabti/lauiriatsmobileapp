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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

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
            public void onClick(View v) {
                read_json_array();
//                String ok = URL_TextEdit.getText().toString();
//                result_http_client.setText("");
//                connecting_rest(ok);
            }});
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
    private void read_json(){
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("formules");
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                result_http_client.append("formule : "+jo_inside.getString("formule")+"  url value :  "+jo_inside.getString("url")+"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();result_http_client.append(e.toString());
        }
    }
    private void read_json_array(){
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                result_http_client.append("objet "+(i+1)+"  begin \n\n");
                result_http_client.append("nom : "+jo_inside.getString("nom"));
                result_http_client.append("  id :  "+jo_inside.getInt("id"));
                result_http_client.append("  genre :  "+jo_inside.getString("genre"));
                result_http_client.append("  longitude :  "+jo_inside.get("longitude"));
                result_http_client.append("  mail :  "+jo_inside.getString("email"));
                result_http_client.append("  photo base 64 :  "+jo_inside.getString("image"));
                result_http_client.append("\n\n");
            }
        } catch (Exception e) {
            e.printStackTrace();result_http_client.append(e.toString());
        }
    }
    private String loadJSONFromAsset() {
        String json;
        try {
            File myExternalFile = new File(Objects.requireNonNull(getActivity()).getExternalFilesDir("Annuaire"), "myjson.json");
            FileInputStream fis = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fis);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}