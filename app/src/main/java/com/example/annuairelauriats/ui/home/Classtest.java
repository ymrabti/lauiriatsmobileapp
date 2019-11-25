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
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
public class Classtest  {
    public static int getLastID(Context context) throws Exception {
        JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,"myjson.json"));
        int pa =0;
        for (int i = 0; i < m_jArry.length(); i++) {
            JSONObject jo_inside = m_jArry.getJSONObject(i);
            pa = jo_inside.getInt("id")+1;
        }
        return pa;
    }
    public static void read_json_array(Context context,JSONObject jsonObject){
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,"myjson.json"));
            m_jArry.put(jsonObject);
            write_file_data(context,m_jArry.toString(),"myjson.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void write_file_data(Context context,String textToWrite,String filename) {
        try {
            File myExternalFile = new File(context.getExternalFilesDir("Annuaire"), filename);
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
                            peupler_array_list(context,findbyfiliere.getSelectedItem().toString(),listView);
                        }
                        else
                        {
                            show_laureats_on_map(context,findbyfiliere.getSelectedItem().toString(),googleMap);
                        }
                        dialogFilter.dismiss();
                    }
                }
        );
        dialogFilter.setCancelable(false);
        dialogFilter.show();
    }
    public static void peupler_array_list(Context context, String filiere, ListView malist){
        ArrayList<Laureat> laureats = new ArrayList<>();
        try {
            JSONArray m_jArry = new JSONArray(Classtest.loadJSONFromAsset(context,"myjson.json"));
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                if (filiere.equals("ALL")){

                    laureats.add(
                            new Laureat(jo_inside.getString("image")+"",
                                    jo_inside.getString("nom")+" "+jo_inside.getString("prenom"),
                                    jo_inside.getString("organisme")+"", jo_inside.getString("email")+""));
                }
                else{
                    if (jo_inside.getString("filiere").equals(filiere)){
                        laureats.add(
                                new Laureat(jo_inside.getString("image")+"",
                                        jo_inside.getString("nom")+" "+jo_inside.getString("prenom"),
                                        jo_inside.getString("organisme")+"", jo_inside.getString("email")+""));
                    }
                }
            }
            //Laureat laureat_vide = new Laureat("","","","");
            //laureats.add(laureat_vide);
            LaureatAdapter adaptateur = new LaureatAdapter(context, laureats);
            malist.setAdapter(adaptateur);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void show_laureats_on_map(Context context,String filiere, GoogleMap gmap){
        List<LatLng> latLngs= peupler_list_latslongs(context,filiere);
        gmap.clear();
        for(int i=0;i<latLngs.size();i++){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLngs.get(i));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            gmap.addMarker(markerOptions);
        }
    }
    private static List<LatLng> peupler_list_latslongs(Context context,String filiere){
        List<LatLng> list_a_peupler = new ArrayList<>();
        try {
            JSONArray m_jArry = new JSONArray(Classtest.loadJSONFromAsset(context,"myjson.json"));
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                if (filiere.equals("ALL")){
                    list_a_peupler.add(new LatLng(jo_inside.getDouble("latitude"),jo_inside.getDouble("longitude")));
                }
                else{
                    if (jo_inside.getString("filiere").equals(filiere)){
                        list_a_peupler.add(new LatLng(jo_inside.getDouble("latitude"),jo_inside.getDouble("longitude")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list_a_peupler;
    }
}
