package com.example.annuairelauriats.ui.home;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

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
import java.util.Calendar;
import java.util.List;

import static com.example.annuairelauriats.ui.gallery.GalleryFragment.laureats_list;

public class Classtest  extends AppCompatActivity {
    public static int id_connected,id_selected;
    public static String
            laureats="laureats.json",filter="filter.json",genders ="genders.json",posts="posts.json",
            provinces="provinces.json",roles="roles.json",secteurs="secteurs.json",organismes="organismes.json",
            org_en_attente="org_en_attente.json",org_laureat="org_laureat.json",
            folder = "Annuaire",images_file="images.json",filiers="filieres.json",promotions="promotions.json";


    @SuppressLint("StaticFieldLeak")
    public static Bitmap base64toImage(final String imageString){
        Bitmap new_bitmap;
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        new_bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return new_bitmap;
    }

    public static int getLastID(Context context,String filename) throws Exception {
        JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,filename));
        int pa =0;
        for (int i = 0; i < m_jArry.length(); i++) {
            JSONObject jo_inside = m_jArry.getJSONObject(i);
            pa = jo_inside.getInt("id")+1;
        }
        return pa;
    }

    public static JSONObject getJsonObjectBycle(Context context,String cle,long id,String filename) throws Exception {
        JSONArray m_jArray = new JSONArray(loadJSONFromAsset(context,filename)) ;
        JSONObject jsonObject=new JSONObject();
        for (int i = 0; i < m_jArray.length(); i++) {
            JSONObject jo_inside = m_jArray.getJSONObject(i);
            if(jo_inside.getInt(cle)==id){jsonObject=jo_inside;}
        }
        return jsonObject;
    }

    public static JSONObject getJsonObjectBykey(Context context,String cle,String valeur,String filename) throws Exception {
        JSONArray m_jArray = new JSONArray(loadJSONFromAsset(context,filename)) ;
        JSONObject jsonObject=new JSONObject();
        for (int i = 0; i < m_jArray.length(); i++) {
            JSONObject jo_inside = m_jArray.getJSONObject(i);
            if(jo_inside.getString(cle).equals(valeur)){jsonObject=jo_inside;}
        }
        return jsonObject;
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
        else{list_a_peupler.add("SELECTIONNER");}
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

    public static void promotion_peuplement(Context context,long id,Spinner spinner) throws Exception {
        ArrayList<String> promos_filier = new ArrayList<>();promos_filier.add("SELECTIONNER");
        Calendar rightNow = Calendar.getInstance();
        if (id!=0){
            JSONObject Filiere_Selected = getJsonObjectBycle(context,"id",id,filiers);
            final int premier_promotion= Integer.parseInt(Filiere_Selected.getString("Date_Creation").substring(0,4).trim());
            for (int i=premier_promotion;i<=rightNow.get(Calendar.YEAR);i++){
                promos_filier.add(i+"");
            }
        }
        ArrayAdapter<String> list_adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,promos_filier);
        list_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(list_adapter);
    }

    public static String loadJSONFromAsset(Context context,String fichier) {
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

    public static void write_file_cache(Context context,
                                        long organisme,String province,
                                        long filiere,String promotion,String secteur) throws Exception {
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("organisme",organisme);jsonObject.put("province",province);
        jsonObject.put("filiere",filiere);jsonObject.put("promotion",promotion);
        jsonObject.put("secteur",secteur);
        write_file_data(context,jsonObject.toString(),filter);
    }

    public static void ShowPopupfilter(final Context context, final ListView listView, final GoogleMap googleMap, final int mark) {
        final Dialog dialogFilter = new Dialog(context);
        dialogFilter.setContentView(R.layout.filter_pop_up_liste);
        final Spinner findbyfiliere = dialogFilter.findViewById(R.id.snipper_filtre_laureat_filiere);
        final Spinner findbypromotion = dialogFilter.findViewById(R.id.snipper_filtre_laureat_promotion);
        final Spinner findbyprovince = dialogFilter.findViewById(R.id.snipper_filtre_laureat_province);
        final Spinner findbyorganisation = dialogFilter.findViewById(R.id.snipper_filtre_laureat_organisation);
        final Spinner findbysecteur = dialogFilter.findViewById(R.id.snipper_filtre_laureat_secteur);
        Classtest.spinner_list_adapt(context,findbyfiliere,"Nom",filiers,1);
        Classtest.spinner_list_adapt(context,findbypromotion,"promotion",promotions,1);
        Classtest.spinner_list_adapt(context,findbyprovince,"province",provinces,1);
        Classtest.spinner_list_adapt(context,findbysecteur,"secteur",secteurs,1);
        Classtest.spinner_list_adapt(context,findbyorganisation,"org",organismes,1);
        dialogFilter.findViewById(R.id.button_dismiss_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dialogFilter.dismiss(); }});
        findbyorganisation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position!=0){findbysecteur.setEnabled(false);}
            else {findbysecteur.setEnabled(true);} }
            @Override public void onNothingSelected(AdapterView<?> parentView) {}});
        findbysecteur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position!=0){findbyorganisation.setEnabled(false);}
            else{findbyorganisation.setEnabled(true);}}
            @Override public void onNothingSelected(AdapterView<?> parentView) {}});

        dialogFilter.findViewById(R.id.button_save_filter).setOnClickListener(
        new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mark==1){
                    peupler_array_list(context,
                            findbyfiliere.getSelectedItemId(),
                            findbypromotion.getSelectedItem().toString(),
                            findbyprovince.getSelectedItem().toString(),
                            findbyorganisation.getSelectedItemId(),findbysecteur.getSelectedItem().toString(),listView);
                }
                else
                {
                    show_laureats_on_map(context,findbyfiliere.getSelectedItem().toString()+"",
                            findbypromotion.getSelectedItem().toString()+"",
                            findbyprovince.getSelectedItem().toString()+""
                            ,googleMap);
                }
                try {
                    write_file_cache(context,findbyorganisation.getSelectedItemId(),
                            findbyprovince.getSelectedItem().toString(),
                            findbyfiliere.getSelectedItemId(),findbypromotion.getSelectedItem().toString()
                            ,findbysecteur.getSelectedItem().toString());
                } catch (Exception e) {
                    e.printStackTrace();
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
                    if (jo_inside.getString(cle).equals(valeur) ){
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

    public static void peupler_array_list(Context context,
                                          long filiere, String promotion,
                                          String province, long organisation,
                                          String secteur, ListView malist){
        laureats_list = new ArrayList<>();
        try {
            /*JSONArray m_jArry = new JSONArray(Classtest.loadJSONFromAsset(context,laureats));
            JSONArray filiere_checked = checkFieldInteger("filiere",filiere,m_jArry);
            JSONArray promotion_filiere_checked = checkField("promotion",promotion,filiere_checked);
            JSONArray province_checked = checkField("province",province,promotion_filiere_checked);
            for (int i = 0; i < promotion_filiere_checked.length(); i++) {
                JSONObject jo_inside = promotion_filiere_checked.getJSONObject(i);
                JSONObject image= getJsonObjectBycle(context,"laureat",jo_inside.getInt("id"),images_file);
                laureats_list.add(
                        new Laureat(jo_inside.getInt("id"),image.getString("image")+"",
                                jo_inside.getString("nom")+" "+jo_inside.getString("prenom"),
                                jo_inside.getString("email")+"", jo_inside.getString("description")+""));
            }*/

            JSONArray orgs = new JSONArray(Classtest.loadJSONFromAsset(context,organismes));
            JSONArray orgs_laureats = new JSONArray(Classtest.loadJSONFromAsset(context,org_laureat));
            JSONArray laureats_finaux = new JSONArray();
            if (organisation==0) {
                JSONArray secteur_checked = checkField("secteur",secteur,orgs);
                for (int i = 0; i < secteur_checked.length(); i++){
                    JSONObject jsonObject = secteur_checked.getJSONObject(i);
                    long id_org = jsonObject.getInt("id");
                    JSONArray jsonArray = checkFieldInteger("id_org",id_org,orgs_laureats);
                    for (int j = 0; j < jsonArray.length();j++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                        long id_laureat = jsonObject1.getInt("id_laureat");
                        JSONObject laureat_courant = getJsonObjectBycle(context,"id",id_laureat,laureats);
                        laureats_finaux.put(laureat_courant);
                    }
                }
            }
            else if (secteur.equals("TOUT")){
                JSONArray jsonArray = checkFieldInteger("id_org",organisation,orgs_laureats);
                for (int j = 0; j < jsonArray.length();j++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                    long id_laureat = jsonObject1.getInt("id_laureat");
                    JSONObject laureat_courant = getJsonObjectBycle(context,"id",id_laureat,laureats);
                    laureats_finaux.put(laureat_courant);
                }
            }
            for (int i=0;i<laureats_finaux.length();i++){
                JSONObject laureat_courant = laureats_finaux.getJSONObject(i);
                long id_laureat = laureat_courant.getInt("id");
                JSONObject image= getJsonObjectBycle(context,"laureat",id_laureat,images_file);
                if (filiere!=0 && !promotion.equals("TOUT")){
                    if (laureat_courant.getString("promotion").equals(promotion) && laureat_courant.getInt("filiere")==filiere){
                        laureats_list.add(
                                new Laureat(laureat_courant.getInt("id"),image.getString("image")+"",
                                        laureat_courant.getString("nom")+" "+laureat_courant.getString("prenom"),
                                        laureat_courant.getString("email")+"", laureat_courant.getString("description")+""));
                    }
                }
                else if (filiere==0 && !promotion.equals("TOUT")){
                    if (laureat_courant.getString("promotion").equals(promotion)){
                        laureats_list.add(
                                new Laureat(laureat_courant.getInt("id"),image.getString("image")+"",
                                        laureat_courant.getString("nom")+" "+laureat_courant.getString("prenom"),
                                        laureat_courant.getString("email")+"", laureat_courant.getString("description")+""));
                    }
                }
                else if (filiere!=0){
                    if (laureat_courant.getInt("filiere")==filiere){
                        laureats_list.add(
                                new Laureat(laureat_courant.getInt("id"),image.getString("image")+"",
                                        laureat_courant.getString("nom")+" "+laureat_courant.getString("prenom"),
                                        laureat_courant.getString("email")+"", laureat_courant.getString("description")+""));
                    }
                }
                else {
                    laureats_list.add(
                            new Laureat(laureat_courant.getInt("id"),image.getString("image")+"",
                                    laureat_courant.getString("nom")+" "+laureat_courant.getString("prenom"),
                                    laureat_courant.getString("email")+"", laureat_courant.getString("description")+""));
                }
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
        if (promotion.equals("SELECTIONNER")){new_Laureat.put("promotion","2020");}
        else{new_Laureat.put("promotion",promotion);}
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

    public static boolean is_login_valid(Context context,String email,String password)  throws Exception{
        JSONObject jsonObject = getJsonObjectBykey(context,"email",email,laureats);
        return !jsonObject.isNull("id") && jsonObject.getString("password").equals(password);
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

    public static void read_json_array(Context context,JSONObject jsonObject,String filename){
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,filename));
            m_jArry.put(jsonObject);
            write_file_data(context,m_jArry.toString(),filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
