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
import org.json.JSONArray;
import org.json.JSONException;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class HomeFragment extends Fragment  {
    private TextView result_http_client;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //HomeViewModel homeViewModel;
        //homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        String url = "http://192.168.137.1:8090/laureat_api/Laureat/";
        final TextView URL_TextEdit = root.findViewById(R.id.editText1);URL_TextEdit.append(url);
        result_http_client=root.findViewById(R.id.result_http_client);
        Button but_connect = root.findViewById(R.id.buttonSend);

        but_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //read__file_data();
                //write_file_data("[{\"promotion\":1999}]");
                read_json_array();
            }});

        return root;
    }
    /*
    private void write_file_data(String textToWrite) {
        try {
            File myExternalFile = new File(Objects.requireNonNull(getActivity()).getExternalFilesDir("Annuaire"), filenameExternal);
            FileOutputStream fos = new FileOutputStream(myExternalFile);
            fos.write(textToWrite.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void read__file_data(){
        try {
            StringBuilder myData = new StringBuilder();
            File myExternalFile = new File(Objects.requireNonNull(getActivity()).getExternalFilesDir("Annuaire"), filenameExternal);
            FileInputStream fis = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData.append(strLine);
            }
            in.close();
            result_http_client.setText(myData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int getLastPromofileplusun() throws Exception {
        JSONArray m_jArry = getJSONarray();int pa =0;
        for (int i = 0; i < m_jArry.length(); i++) {
            JSONObject jo_inside = m_jArry.getJSONObject(i);
            pa = jo_inside.getInt("promotion");
        }
        return pa;
    }*/

    private void read_json_array(){
        try {
            JSONArray m_jArry = getJSONarray();
            //JSONObject jsonObject = new JSONObject();
            //jsonObject.put("promotion",value);
            //m_jArry.put(jsonObject);
            //write_file_data(m_jArry.toString());
            result_http_client.append(m_jArry.toString());
            //read__file_data();
        } catch (Exception e) {
            e.printStackTrace();result_http_client.append(e.toString()+"   hna   \n");
        }
    }

    private JSONArray getJSONarray() throws JSONException {
        return new JSONArray(loadJSONFromAsset());
    }

    private String loadJSONFromAsset() {
        String json;
        try {
            String filenameExternal = "promotions.json";
            File myExternalFile = new File(Objects.requireNonNull(getActivity()).getExternalFilesDir("Annuaire"), filenameExternal);
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
/*
    public void connecting_rest(final String url) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                *//*HttpClient httpClient = new DefaultHttpClient();
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
                }*//*

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
    }*/
}