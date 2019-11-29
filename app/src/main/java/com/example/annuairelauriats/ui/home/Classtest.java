package com.example.annuairelauriats.ui.home;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.io.ByteArrayOutputStream;
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

public class Classtest  extends AppCompatActivity {
    private static int id_connected,id_selected;
    public static String laureats="laureats.json",
            organismes="organismes.json",org_en_attente="org_en_attente.json",org_laureat="org_laureat.json",
            folder = "Annuaire",images_file="images.json";
    public static int getLastID(Context context,String filename) throws Exception {
        JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,filename));
        int pa =0;
        for (int i = 0; i < m_jArry.length(); i++) {
            JSONObject jo_inside = m_jArry.getJSONObject(i);
            pa = jo_inside.getInt("id")+1;
        }
        return pa;
    }

    public static JSONObject getJsonObjectBycle(Context context,String cle,int id,String filename) throws Exception {
        JSONArray m_jArray = new JSONArray(loadJSONFromAsset(context,filename)) ;
        JSONObject jsonObject=new JSONObject();
        for (int i = 0; i < m_jArray.length(); i++) {
            JSONObject jo_inside = m_jArray.getJSONObject(i);
            if(jo_inside.getInt(cle)==id){jsonObject=jo_inside;}
        }
        return jsonObject;
    }

    public static void read_json_array(Context context,JSONObject jsonObject,String filename){
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,filename));
            m_jArry.put(jsonObject);
            write_file_data(context,m_jArry.toString(),filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static void write_file_data(Context context,String textToWrite,String filename) {
        try {
            File myExternalFile = new File(context.getExternalFilesDir(folder), filename);
            FileOutputStream fos = new FileOutputStream(myExternalFile);
            fos.write(textToWrite.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    //ECRIRE UN CONTENUE DANS UN FICHIER

    public static void spinner_list_adapt(Context context, Spinner spinner, String champ, String fichier,int mark){
        List<String> list_items = peupler_list(context,champ,fichier,mark);
        ArrayAdapter<String> list_adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,list_items);
        list_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(list_adapter);
    }  //ADAPTER UNE LIST A UN SPINNER

    private static List<String> peupler_list(Context context,String Champ,String file,int mark){
        List<String> list_a_peupler = new ArrayList<>();
        if (mark==1){list_a_peupler.add("TOUT");}
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,file));
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                list_a_peupler.add(jo_inside.getString(Champ)+"");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list_a_peupler;
    }   //RETOURNE UNE LIST PEUPLE PRET POUR DROP DOWN

    private static String loadJSONFromAsset(Context context,String fichier) {
        String json;
        try {
            File myExternalFile = new File(context.getExternalFilesDir(folder), fichier);
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
    }         //LIRE CONTENUE D UN FICHIER

    public static void ShowPopupfilter(final Context context, final ListView listView, final GoogleMap googleMap, final int mark) {
        final Dialog dialogFilter = new Dialog(context);
        dialogFilter.setContentView(R.layout.filter_pop_up_liste);
        final Spinner findbyfiliere = dialogFilter.findViewById(R.id.snipper_filtre_laureat_filiere);
        final Spinner findbypromotion = dialogFilter.findViewById(R.id.snipper_filtre_laureat_promotion);
        final Spinner findbyprovince = dialogFilter.findViewById(R.id.snipper_filtre_laureat_province);
        Classtest.spinner_list_adapt(context,findbyfiliere,"Nom","filieres.json",1);
        Classtest.spinner_list_adapt(context,findbypromotion,"promotion","promotions.json",1);
        Classtest.spinner_list_adapt(context,findbyprovince,"province","provinces.json",1);
        dialogFilter.findViewById(R.id.button_dismiss_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFilter.dismiss();
            }
        });
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
                            findbyfiliere.getSelectedItemId(),
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
    }  //AFFICHE POPUP POUR UN FILTRE

    private static JSONArray checkField(String cle,String valeur,JSONArray m_jArry){
        JSONArray array_checked = new JSONArray();
        if (!valeur.equals("TOUT")){
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

    private static JSONArray checkFieldInteger(String cle,long valeur,JSONArray m_jArry){
        JSONArray array_checked = new JSONArray();
        if (valeur!=0){
            try {
                for (int i = 0; i < m_jArry.length(); i++) {
                    JSONObject jo_inside = m_jArry.getJSONObject(i);
                    if (jo_inside.getInt(cle)==valeur){
                        array_checked.put(jo_inside);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else{
            array_checked = m_jArry;
        }
        return array_checked;
    }           //RETOUNE ARRAYJSON APRES VERIFICATION CLE VALEUR

    public static void peupler_array_list(Context context, long filiere,String promotion,String province, ListView malist){
        ArrayList<Laureat> laureats_list = new ArrayList<>();
        try {
            JSONArray m_jArry = new JSONArray(Classtest.loadJSONFromAsset(context,laureats));
            JSONArray images= new JSONArray(loadJSONFromAsset(context,images_file)) ;
            JSONArray filiere_checked = checkFieldInteger("filiere",filiere,m_jArry);
            JSONArray promotion_filiere_checked = checkField("promotion",promotion,filiere_checked);
            //JSONArray province_checked = checkField("province",province,promotion_checked);
            for (int i = 0; i < promotion_filiere_checked.length(); i++) {
                JSONObject jo_inside = promotion_filiere_checked.getJSONObject(i);
                JSONObject image= getJsonObjectBycle(context,"laureat",jo_inside.getInt("id"),images_file);
                laureats_list.add(
                        new Laureat(image.getString("image")+"",
                                jo_inside.getString("nom")+" "+jo_inside.getString("prenom"),
                                jo_inside.getString("email")+"", jo_inside.getString("description")+""));
            }
            LaureatAdapter adaptateur = new LaureatAdapter(context, laureats_list);
            malist.setAdapter(adaptateur);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   //AFIICHE LA LIST APRES LE FILTRE

    public static void show_laureats_on_map(Context context,String filiere,String promotion,String province, GoogleMap gmap){
        List<LatLng> latLngs= peupler_list_latslongs(context,filiere,promotion,province,organismes);
        List<LatLng> latLngListe = peupler_list_latslongs(context,filiere,promotion,province,org_en_attente);
        gmap.clear();
        for(int i=0;i<latLngs.size();i++){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLngs.get(i));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            gmap.addMarker(markerOptions);
        }
        for(int i=0;i<latLngListe.size();i++){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLngListe.get(i));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            gmap.addMarker(markerOptions);
        }
    }//AFFICHER LIST DES LATLON SUR LA CARTE

    private static List<LatLng> peupler_list_latslongs(Context context,String filiere,String promotion,String province,String filename){
        List<LatLng> list_a_peupler = new ArrayList<>();
        try {
            JSONArray m_jArry = new JSONArray(Classtest.loadJSONFromAsset(context,filename));
            //JSONArray filiere_checked = checkField("filiere",filiere,m_jArry);
            //JSONArray filiere_promotion_checked = checkField("promotion",promotion,filiere_checked);
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                list_a_peupler.add(new LatLng(jo_inside.getDouble("latitude"),jo_inside.getDouble("longitude")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list_a_peupler;
    }   //RETOURNE UNE LISTE DE LATLON

    public static void new_Laureat_Register(
            Context context,int id, String nom, String prenom, String gender, String promotion, long filiere, String email,
            String password, String tel, String dateNow, String description) throws Exception {
        JSONObject new_Laureat = new JSONObject();
        new_Laureat.put("id",id);
        new_Laureat.put("nom",nom);
        new_Laureat.put("prenom",prenom);
        new_Laureat.put("genre",gender);
        new_Laureat.put("promotion",promotion);
        new_Laureat.put("filiere",filiere);
        new_Laureat.put("email",email);
        new_Laureat.put("password",password);
        new_Laureat.put("telephone",tel);
        new_Laureat.put("roleName",1);
        new_Laureat.put("date_insc", dateNow);
        new_Laureat.put("description",description);
        new_Laureat.put("actif",true);
        JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,laureats));
        m_jArry.put(new_Laureat);
        write_file_data(context,m_jArry.toString(),laureats);
    }

    public static void setNewImgLaureat(Context context,String image,int laureat) throws Exception{
        JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,images_file));
        JSONObject nouveau_image = new JSONObject();
        nouveau_image.put("id",laureat);
        nouveau_image.put("current",true);
        nouveau_image.put("laureat",laureat);
        nouveau_image.put("image",image);
        /*JSONObject old_image = getJsonObjectBycle("laureat",laureat,m_jArry);
        int id_old_image = old_image.getInt("id");old_image.put("current",false);
        m_jArry.remove(id_old_image);m_jArry.put(old_image);*/
        m_jArry.put(nouveau_image);
        write_file_data(context,m_jArry.toString(),images_file);
    }

    public static void new_org_attente_admin(Context context,String nom,int laureat,double lat,double lon,String secteur) throws Exception {
        if (!nom.isEmpty()){
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,org_en_attente));
            JSONObject nouveau_org = new JSONObject();
            nouveau_org.put("id",getLastID(context,org_en_attente));
            nouveau_org.put("org",nom);
            nouveau_org.put("laureat",laureat);
            nouveau_org.put("latitude",lat);
            nouveau_org.put("longitude",lon);
            nouveau_org.put("secteur",secteur);
            m_jArry.put(nouveau_org);write_file_data(context,m_jArry.toString(),org_en_attente);}
    }

    public static void new_org_laureat(Context context,long id_org_selected,int laureat,String date_debut,String fonction) throws Exception {
        JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,org_laureat));
        JSONObject nouveau_org = new JSONObject();
        nouveau_org.put("id_laureat",laureat);
        nouveau_org.put("id_org",id_org_selected);
        nouveau_org.put("date_debut",date_debut);
        nouveau_org.put("en_cours",true);
        nouveau_org.put("intitule_fonction",fonction);
        m_jArry.put(nouveau_org);write_file_data(context,m_jArry.toString(),org_laureat);
    }

    public static void changefieldvalue(Context context,boolean newvalue,String cle,int id){
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,laureats));
            JSONObject user_actuelle = m_jArry.getJSONObject(id);
            user_actuelle.put("id",newvalue);
            m_jArry.remove(id);m_jArry.put(user_actuelle);
            write_file_data(context,m_jArry.toString(),laureats);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }                                               //CHANGER LA STATUS D UN LAUREAT

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
