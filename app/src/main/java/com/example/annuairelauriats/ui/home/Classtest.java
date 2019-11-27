package com.example.annuairelauriats.ui.home;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.gallery.Laureat;
import com.example.annuairelauriats.ui.gallery.LaureatAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
public class Classtest  {
    private static int id_connected,id_selected;
    public static int getLastID(Context context) throws Exception {
        JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,"myjson.json"));
        int pa =0;
        for (int i = 0; i < m_jArry.length(); i++) {
            JSONObject jo_inside = m_jArry.getJSONObject(i);
            pa = jo_inside.getInt("id")+1;
        }
        return pa;
    }
    public static JSONObject getJsonObjectById(Context context,int id) throws Exception {
        JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,"myjson.json"));
        JSONObject jsonObject=new JSONObject();
        for (int i = 0; i < m_jArry.length(); i++) {
            JSONObject jo_inside = m_jArry.getJSONObject(i);
            if(jo_inside.getInt("id")==id){jsonObject=jo_inside;}
        }
        return jsonObject;
    }
    public static void read_json_array(Context context,JSONObject jsonObject){
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,"myjson.json"));
            m_jArry.put(jsonObject);
            write_file_data(context,m_jArry.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void write_file_data(Context context,String textToWrite) {
        try {
            File myExternalFile = new File(context.getExternalFilesDir("Annuaire"), "myjson.json");
            FileOutputStream fos = new FileOutputStream(myExternalFile);
            fos.write(textToWrite.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void spinner_list_adapt(Context context, Spinner spinner, String champ, String fichier,int mark){
        List<String> list_items = peupler_list(context,champ,fichier,mark);
        ArrayAdapter<String> list_adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,list_items);
        list_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(list_adapter);
    }
    private static List<String> peupler_list(Context context,String Champ,String file,int mark){
        List<String> list_a_peupler = new ArrayList<>();
        if (mark==1){list_a_peupler.add("ALL");}
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,file));
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                list_a_peupler.add(jo_inside.getString(Champ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list_a_peupler;
    }
    private static String loadJSONFromAsset(Context context,String fichier) {
        String json;
        try {
            File myExternalFile = new File(context.getExternalFilesDir("Annuaire"), fichier);
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
    public static void ShowPopupfilter(final Context context, final ListView listView, final GoogleMap googleMap, final int mark) {
        final Dialog dialogFilter = new Dialog(context);
        dialogFilter.setContentView(R.layout.filter_pop_up_liste);
        final Spinner findbyfiliere = dialogFilter.findViewById(R.id.snipper_filtre_laureat_filiere);
        final Spinner findbypromotion = dialogFilter.findViewById(R.id.snipper_filtre_laureat_promotion);
        final Spinner findbyprovince = dialogFilter.findViewById(R.id.snipper_filtre_laureat_province);
        Classtest.spinner_list_adapt(context,findbyfiliere,"filiere","filieres.json",1);
        Classtest.spinner_list_adapt(context,findbypromotion,"promotion","promotions.json",1);
        Classtest.spinner_list_adapt(context,findbyprovince,"province","provinces.json",1);
        dialogFilter.findViewById(R.id.button_save_filter).setOnClickListener(
        new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,
                        "filiere : "+findbyfiliere.getSelectedItem()
                                +"\npromotion : "+findbypromotion.getSelectedItem()
                                +"\nprovince "+ findbyprovince.getSelectedItem(),
                        Toast.LENGTH_LONG).show();
                if (mark==1){
                    peupler_array_list(context,
                            findbyfiliere.getSelectedItem().toString(),
                            findbypromotion.getSelectedItem().toString(),
                            findbyprovince.getSelectedItem().toString(),listView);
                }
                else
                {
                    show_laureats_on_map(context,findbyfiliere.getSelectedItem().toString()+"",
                            findbypromotion.getSelectedItem().toString()+"",
                            findbyprovince.getSelectedItem().toString()+""
                            ,googleMap);
                }
                dialogFilter.dismiss();
            }
        }
        );
        dialogFilter.setCancelable(false);
        dialogFilter.show();
    }
    private static JSONArray checkField(String cle,String valeur,JSONArray m_jArry){
        JSONArray array_checked = new JSONArray();
        if (!valeur.equals("ALL")){
            for (int i = 0; i < m_jArry.length(); i++) {
                try {
                    JSONObject jo_inside = m_jArry.getJSONObject(i);
                    if (jo_inside.getString(cle).equals(valeur)){
                        array_checked.put(jo_inside);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            array_checked = m_jArry;
        }
        return array_checked;
    }
    public static void peupler_array_list(Context context, String filiere,String promotion,String province, ListView malist){
        ArrayList<Laureat> laureats = new ArrayList<>();
        try {
            JSONArray m_jArry = new JSONArray(Classtest.loadJSONFromAsset(context,"myjson.json"));
            JSONArray filiere_checked = checkField("filiere",filiere,m_jArry);
            JSONArray promotion_filiere_checked = checkField("promotion",promotion,filiere_checked);
            //JSONArray province_checked = checkField("province",province,promotion_checked);
            for (int i = 0; i < promotion_filiere_checked.length(); i++) {
                JSONObject jo_inside = promotion_filiere_checked.getJSONObject(i);
                laureats.add(
                        new Laureat(jo_inside.getString("image")+"",
                                jo_inside.getString("nom")+" "+jo_inside.getString("prenom"),
                                jo_inside.getString("organisme")+"", jo_inside.getString("email")+""));
            }
            LaureatAdapter adaptateur = new LaureatAdapter(context, laureats);
            malist.setAdapter(adaptateur);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void show_laureats_on_map(Context context,String filiere,String promotion,String province, GoogleMap gmap){
        List<LatLng> latLngs= peupler_list_latslongs(context,filiere,promotion,province);
        gmap.clear();
        for(int i=0;i<latLngs.size();i++){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLngs.get(i));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            gmap.addMarker(markerOptions);
        }
    }
    private static List<LatLng> peupler_list_latslongs(Context context,String filiere,String promotion,String province){
        List<LatLng> list_a_peupler = new ArrayList<>();
        try {
            JSONArray m_jArry = new JSONArray(Classtest.loadJSONFromAsset(context,"myjson.json"));
            JSONArray filiere_checked = checkField("filiere",filiere,m_jArry);
            JSONArray filiere_promotion_checked = checkField("promotion",promotion,filiere_checked);
            for (int i = 0; i < filiere_promotion_checked.length(); i++) {
                JSONObject jo_inside = filiere_promotion_checked.getJSONObject(i);
                list_a_peupler.add(new LatLng(jo_inside.getDouble("latitude"),jo_inside.getDouble("longitude")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list_a_peupler;
    }
    public static void setUserStatus(Context context,boolean status,int id){
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,"myjson.json"));
            JSONObject user_actuelle = m_jArry.getJSONObject(id);
            user_actuelle.put("id",status);
            m_jArry.remove(id);m_jArry.put(user_actuelle);
            write_file_data(context,m_jArry.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int getId_connected() { return id_connected; }
    public static void setId_connected(int id_connected) { Classtest.id_connected = id_connected; }

    public static int getId_selected() { return id_selected; }
    public static void setId_selected(int id_selected) { Classtest.id_selected = id_selected; }
    /*private void read_json(){
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("formules");
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    /*

    public void connecting_rest(final String url) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(url);
                HttpResponse response;
                try {
                    response = httpClient.execute(httpget);
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream instream = entity.getContent();
                        String result= convertStreamToString(instream);
                        instream.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
*/
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
