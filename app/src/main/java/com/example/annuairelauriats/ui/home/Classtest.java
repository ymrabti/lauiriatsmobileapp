package com.example.annuairelauriats.ui.home;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.gallery.Laureat;
import com.example.annuairelauriats.ui.gallery.LaureatAdapter;
import com.example.annuairelauriats.ui.login.LoginActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static com.example.annuairelauriats.ui.gallery.GalleryFragment.laureats_list;
import static java.lang.Math.min;

public class Classtest  extends AppCompatActivity {
    public static int id_connected,id_selected;private static JSONObject result;
    private static VolleyError volleyError;private static JSONArray resultat ;
    public static String laureats="laureats.json",filter="filter.json",genders ="genders.json",posts="posts.json",
            provinces="provinces.json",roles="roles.json",secteurs="secteurs.json",organismes="organismes.json",
            org_en_attente="org_en_attente.json",org_laureat="org_laureat.json",
            folder = "Annuaire",images_file="images.json",filiers="filieres.json"
            ,ip_server="http://192.168.137.1:3000";
    @SuppressLint("StaticFieldLeak")
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
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }         //LIRE CONTENUE D UN FICHIER
    public static boolean is_file_exists(Context context,String fichier) {
        File myExternalFile = new File(context.getExternalFilesDir(folder), fichier);
        return myExternalFile.exists();
    }         //LIRE CONTENUE D UN FICHIER
    public static String load_raw(Context context,int id) throws Exception {
        InputStream in_s = context.getResources().openRawResource(id);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in_s));
        String line ;StringBuilder result=new StringBuilder();
        while ((line = reader.readLine()) != null) { result.append(line); }
        return result.toString();
    }
    public static void raw_to_asset(Context context) throws Exception{
        write_file_data(context,load_raw(context,R.raw.filieres),filiers);
        write_file_data(context,load_raw(context,R.raw.filter),filter);
        write_file_data(context,load_raw(context,R.raw.images),images_file);
        write_file_data(context,load_raw(context,R.raw.laureats),laureats);
        write_file_data(context,load_raw(context,R.raw.org_en_attente),org_en_attente);
        write_file_data(context,load_raw(context,R.raw.org_laureat),org_laureat);
        write_file_data(context,load_raw(context,R.raw.organismes),organismes);
        write_file_data(context,load_raw(context,R.raw.posts),posts);
        write_file_data(context,load_raw(context,R.raw.secteurs),secteurs);
        write_file_data(context,load_raw(context,R.raw.genders),genders);
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
    public static int getLastID(Context context,String filename) throws Exception {
        JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,filename));
        int pa =0;
        for (int i = 0; i < m_jArry.length(); i++) {
            JSONObject jo_inside = m_jArry.getJSONObject(i);
            pa = jo_inside.getInt("id")+1;
        }
        return pa;
    }
    public static void new_Laureat_Register(Context context,int id, String nom, String prenom, String gender, String promotion, long filiere, String email, String password, String tel, String dateNow, String description) throws Exception {
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
    public static void new_org_attente_admin(Context context, String nom,int laureat, double lat,double lon, String secteur,String datee,String intitule) throws Exception {
        if (!nom.isEmpty()){
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context,org_en_attente));
            JSONObject nouveau_org = new JSONObject();
            nouveau_org.put("id",getLastID(context,org_en_attente));
            nouveau_org.put("org",nom);
            nouveau_org.put("laureat",laureat);
            nouveau_org.put("latitude",lat);
            nouveau_org.put("longitude",lon);
            nouveau_org.put("secteur",secteur);
            nouveau_org.put("date_debut",datee);
            nouveau_org.put("intitule",intitule);
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


    public static Bitmap get_Bitmap(Context context,int drawableRes) {
        Drawable drawable = context.getDrawable(drawableRes);
        Canvas canvas = new Canvas();int target=50;
        Bitmap bitmap = Bitmap.createBitmap(target, target, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, target, target);
        drawable.draw(canvas);
        return bitmap;
    }
    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int minimum = min(scaleBitmapImage.getWidth(),scaleBitmapImage.getHeight());
        Bitmap targetBitmap = Bitmap.createBitmap(minimum, minimum,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) minimum - 1) / 2, ((float) minimum - 1) / 2,
                (min(((float) minimum), ((float) minimum)) / 2),
                Path.Direction.CCW);


        canvas.clipPath(path);
        canvas.drawBitmap(scaleBitmapImage,
                new Rect(0, 0, minimum, minimum),
                new Rect(0, 0, minimum, minimum), null);
        return targetBitmap;
    }
    public static Bitmap add_text(Bitmap icon,String textToDraw){
        Bitmap newBitmap = icon.copy(icon.getConfig(), true);
        Canvas newCanvas = new Canvas(newBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);float scale = (float)icon.getWidth()/textToDraw.length();
        paint.setTextSize(scale);
        Rect bounds = new Rect();
        paint.getTextBounds(textToDraw, 0, textToDraw.length(), bounds);
        int x = (icon.getWidth() - bounds.width())/2;
        int y = (icon.getHeight() + bounds.height())/2;
        newCanvas.drawText(textToDraw, x, y, paint);
        return  newBitmap;
    }
    public static void ShowPopupfilter(final Context context, final ListView listView, final GoogleMap googleMap, final int mark) {
        final Dialog dialogFilter = new Dialog(context);
        dialogFilter.setContentView(R.layout.filter_pop_up_liste);
        final Spinner findbyfiliere = dialogFilter.findViewById(R.id.snipper_filtre_laureat_filiere);
        final Spinner findbypromotion = dialogFilter.findViewById(R.id.snipper_filtre_laureat_promotion);
        final Spinner findbyprovince = dialogFilter.findViewById(R.id.snipper_filtre_laureat_province);
        final Spinner findbyorganisation = dialogFilter.findViewById(R.id.snipper_filtre_laureat_organisation);
        final Spinner findbysecteur = dialogFilter.findViewById(R.id.snipper_filtre_laureat_secteur);
        spinner_list_adapt(context,findbyfiliere,"Nom",filiers,1);
        spinner_list_adapt(context,findbyprovince,"province",provinces,1);
        spinner_list_adapt(context,findbysecteur,"secteur",secteurs,1);
        spinner_list_adapt(context,findbyorganisation,"org",organismes,1);

        long filiere = get_filter_pref_long(context, "branch");
        try {
            promotion_peuplement(context,filiere,findbypromotion);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long org = get_filter_pref_long(context, "organisation");
        String promo = get_filter_pref_string(context, "promotion");
        String secteur = get_filter_pref_string(context, "sector");
        findbyfiliere.setSelection((int)filiere);
        if (org==0){
            findbysecteur.setSelection(secteur_select(secteur));
        }
        else{findbyorganisation.setSelection((int)org);}
        int promo_selec=0;
        try {
        if (!promo.equals("SELECTIONNER")){
            promo_selec=promotion_premier(context,filiere,promo);
            findbypromotion.setSelection(promo_selec+1);
            Toast.makeText(context,filiere+"\n"+org+"\n"+promo+"\n"+secteur+"\n"+promo_selec+"\n",Toast.LENGTH_LONG).show();
        }
        } catch (Exception e) {
        e.printStackTrace();
    }

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
                                    findbyfiliere.getSelectedItemId(), findbypromotion.getSelectedItem().toString(),
                                    findbyprovince.getSelectedItem().toString(),
                                    findbyorganisation.getSelectedItemId(),findbysecteur.getSelectedItem().toString(),listView);
                        }
                        else
                        {
                            show_laureats_on_map(context,findbyfiliere.getSelectedItemId(), findbypromotion.getSelectedItem().toString()+"",
                                    findbyprovince.getSelectedItem().toString()+"",findbyorganisation.getSelectedItemId(),
                                    findbysecteur.getSelectedItem().toString()+""
                                    ,googleMap);
                        }
                        try {
                            set_filter_pref(context,findbysecteur.getSelectedItem().toString()+""
                                    ,findbypromotion.getSelectedItem().toString()+"", findbyfiliere.getSelectedItemId(),
                                    findbyorganisation.getSelectedItemId()
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialogFilter.dismiss();
                    }
                }
        );
        dialogFilter.setCancelable(false);
        dialogFilter.show();
    }
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
            for (int i=premier_promotion;i<=rightNow.get(Calendar.YEAR)+1;i++){
                promos_filier.add(i+"");
            }
        }
        spinner.setSelection(1);
        ArrayAdapter<String> list_adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,promos_filier);
        list_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(list_adapter);
    } //AFFICHE POPUP POUR UN FILTRE
    public static int promotion_premier(Context context,long id,String actu) throws Exception {
        JSONObject Filiere_Selected = getJsonObjectBycle(context,"id",id,filiers);
        int premier_promotion= Integer.parseInt(Filiere_Selected.getString("Date_Creation").substring(0,4).trim());
        int actue= Integer.parseInt(actu.trim());
        return actue-premier_promotion;
    } //AFFICHE POPUP POUR UN FILTRE
    public static void spinner_list_adapt(Context context, Spinner spinner, String champ, String fichier,int mark){
        List<String> list_items = peupler_list(context,champ,fichier,mark);
        ArrayAdapter<String> list_adapter = new ArrayAdapter<>(context,R.layout.spim,R.id.text_spinner,list_items);
        spinner.setAdapter(list_adapter);
    }  //ADAPTER UNE LIST A UN SPINNER
    public static void peupler_array_list(Context context, long filiere, String promotion, String province, long organisation, String secteur, ListView malist){
        laureats_list = new ArrayList<>();
        try {
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
                JSONArray orgs_attentes = new JSONArray(Classtest.loadJSONFromAsset(context,org_en_attente));
                JSONArray attentes = checkField("secteur",secteur,orgs_attentes);
                for (int i = 0; i < attentes.length(); i++){
                    JSONObject jsonObject = attentes.getJSONObject(i);
                    long id_laureat = jsonObject.getInt("laureat");
                    JSONObject laureat_courant = getJsonObjectBycle(context,"id",id_laureat,laureats);
                    laureats_finaux.put(laureat_courant);
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
                if (id_laureat!=id_connected){
                    JSONObject image= getJsonObjectBycle(context,"laureat",id_laureat,images_file);
                    Laureat laureat_to_add = new Laureat(laureat_courant.getInt("id"),image.getString("image")+"",
                            laureat_courant.getString("nom")+" "+laureat_courant.getString("prenom"),
                            laureat_courant.getString("email")+"", laureat_courant.getString("description")+"");
                    if (filiere!=0 && !promotion.equals("TOUT")){
                        if (laureat_courant.getString("promotion").equals(promotion) && laureat_courant.getInt("filiere")==filiere){
                            laureats_list.add(laureat_to_add);
                        }
                    }
                    else if (filiere==0 && !promotion.equals("TOUT")){
                        if (laureat_courant.getString("promotion").equals(promotion)){
                            laureats_list.add(laureat_to_add);
                        }
                    }
                    else if (filiere!=0){
                        if (laureat_courant.getInt("filiere")==filiere){
                            laureats_list.add(laureat_to_add);
                        }
                    }
                    else {
                        laureats_list.add(laureat_to_add);
                    }
                }
            }
            LaureatAdapter adaptateur = new LaureatAdapter(context, laureats_list);
            malist.setAdapter(adaptateur);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   //AFIICHE LA LIST APRES LE FILTRE
    public static void show_laureats_on_map(Context context,long filiere, String promotion,String province, long organisation,String secteurr, GoogleMap gmap){
        List<Orglatlonid> latLngsIds= peupler_list_latslongs(context,filiere,promotion,province,organisation,secteurr);
        gmap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i=0;i<latLngsIds.size();i++){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLngsIds.get(i).getLatLng());
            markerOptions.title(""+latLngsIds.get(i).getIden());
            Bitmap icoon = getRoundedShape(get_Bitmap(context,R.drawable.circle_shape));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(add_text(icoon,markerOptions.getTitle())));
            builder.include(latLngsIds.get(i).getLatLng());markerOptions.flat(true);
            gmap.addMarker(markerOptions);
        }
        List<Orglatlonid> latLngListe = peupler_list_latslong(context,filiere,promotion,province,secteurr);
         for(int i=0;i<latLngListe.size();i++){
         MarkerOptions markerOptions = new MarkerOptions();
         markerOptions.position(latLngListe.get(i).getLatLng());
         markerOptions.title(""+latLngListe.get(i).getIden());
             try {
                 JSONObject image= getJsonObjectBycle(context,"laureat",latLngListe.get(i).getIden(),images_file);
                 Bitmap icon =resize_icon(base64toImage(image.getString("image")));
                 markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getRoundedShape(icon)));
             } catch (Exception e) {
                 e.printStackTrace();
                 Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                 //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
             }
         builder.include(latLngListe.get(i).getLatLng());markerOptions.flat(false);
         gmap.addMarker(markerOptions);
         }
        LatLngBounds bounds = builder.build();
        int padding = 50;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        gmap.moveCamera(cu);gmap.animateCamera(cu);
    }//AFFICHER LIST DES LATLON SUR LA CARTE
    private static List<Orglatlonid> peupler_list_latslongs(Context context,long filiere, String promotion,String province,long organisation,String secteur){
        List<Orglatlonid> list_orgsIds = new ArrayList<>();
        try {
            JSONArray laureats_finaux = new JSONArray(loadJSONFromAsset(context,laureats));
            for (int i=0;i<laureats_finaux.length();i++){
                JSONObject laureat_courant = laureats_finaux.getJSONObject(i);
                long id_laureat = laureat_courant.getInt("id");
                JSONObject orgorg = getJsonObjectBycle(context,"id_laureat",id_laureat,org_laureat);
                long organ = orgorg.getInt("id_org");
                JSONObject coordinates = getJsonObjectBycle(context,"id",organ,organismes);
                LatLng latLngOrg = new LatLng(coordinates.getDouble("latitude"),
                        coordinates.getDouble("longitude"));
                Orglatlonid orglatlonid = new Orglatlonid(latLngOrg,organ);
                if (filiere!=0 && !promotion.equals("TOUT")){
                    if (laureat_courant.getString("promotion").equals(promotion) && laureat_courant.getInt("filiere")==filiere){
                        if (organisation==0 && !secteur.equals("TOUT")){
                            if (coordinates.getString("secteur").equals(secteur)){
                                list_orgsIds.add(orglatlonid);}
                        }
                        else if ( organisation!=0 && secteur.equals("TOUT")){
                            if (organ==organisation){
                                list_orgsIds.add(orglatlonid);
                            }
                        }
                        else {
                            list_orgsIds.add(orglatlonid);
                        }
                    }
                }
                else if (filiere==0 && !promotion.equals("TOUT")){
                    if (laureat_courant.getString("promotion").equals(promotion) ){
                        if (organisation==0 && !secteur.equals("TOUT")){
                            if (coordinates.getString("secteur").equals(secteur)){
                                list_orgsIds.add(orglatlonid);}
                        }
                        else if ( organisation!=0 && secteur.equals("TOUT")){
                            if (organ==organisation){
                                list_orgsIds.add(orglatlonid);
                            }
                        }
                        else {
                            list_orgsIds.add(orglatlonid);
                        }
                    }
                }
                else if (filiere!=0){
                    if (laureat_courant.getInt("filiere")==filiere){
                        if (organisation==0 && !secteur.equals("TOUT")){
                            if (coordinates.getString("secteur").equals(secteur)){
                                list_orgsIds.add(orglatlonid);}
                        }
                        else if ( organisation!=0 && secteur.equals("TOUT")){
                            if (organ==organisation){
                                list_orgsIds.add(orglatlonid);
                            }
                        }
                        else {
                            list_orgsIds.add(orglatlonid);
                        }
                    }
                }
                else {
                    if (organisation==0 && !secteur.equals("TOUT")){
                        if (coordinates.getString("secteur").equals(secteur)){
                            list_orgsIds.add(orglatlonid);}
                    }
                    else if ( organisation!=0 && secteur.equals("TOUT")){
                        if (organ==organisation){
                            list_orgsIds.add(orglatlonid);
                        }
                    }
                    else {
                        list_orgsIds.add(orglatlonid);
                    }
                }
            }

        }
        catch (Exception e){e.printStackTrace();}
        return list_orgsIds;
    }
    private static List<Orglatlonid> peupler_list_latslong(Context context, long filiere, String promotion, String province, String secteur){
        List<Orglatlonid> list_orgsIds = new ArrayList<>();
        try {
            JSONArray orgs = new JSONArray(loadJSONFromAsset(context,org_en_attente));
            for (int i=0;i<orgs.length();i++){
                JSONObject org_courant = orgs.getJSONObject(i);
                long id_laureat = org_courant.getInt("laureat");
                LatLng latLngOrg = new LatLng(org_courant.getDouble("latitude"),
                        org_courant.getDouble("longitude"));
                Orglatlonid orglatlonid = new Orglatlonid(latLngOrg,id_laureat);
                if (id_laureat!=id_connected){list_orgsIds.add(orglatlonid);}


                /**if (filiere!=0 && !promotion.equals("TOUT")){
                    if (laureat_courant.getString("promotion").equals(promotion) && laureat_courant.getInt("filiere")==filiere){
                        if ( !secteur.equals("TOUT")){
                            if (coordinates.getString("secteur").equals(secteur)){
                                list_a_peupler.add(latLngOrg);}
                        }
                        else {
                            list_a_peupler.add(latLngOrg);
                        }
                    }
                }
                else if (filiere==0 && !promotion.equals("TOUT")){
                    if (laureat_courant.getString("promotion").equals(promotion) ){
                        if ( !secteur.equals("TOUT")){
                            if (coordinates.getString("secteur").equals(secteur)){
                                list_a_peupler.add(latLngOrg);}
                        }
                        else {
                            list_a_peupler.add(latLngOrg);
                        }
                    }
                }
                else if (filiere!=0){
                    if (laureat_courant.getInt("filiere")==filiere){
                        if ( !secteur.equals("TOUT")){
                            if (coordinates.getString("secteur").equals(secteur)){
                                list_a_peupler.add(latLngOrg);}
                        }
                        else {
                            list_a_peupler.add(latLngOrg);
                        }
                    }
                }
                else {
                    if ( !secteur.equals("TOUT")){
                        if (coordinates.getString("secteur").equals(secteur)){
                            list_a_peupler.add(latLngOrg);}
                    }
                    else {
                        list_a_peupler.add(latLngOrg);
                    }
                }*/
            }

        }
        catch (Exception e){e.printStackTrace();}
        return list_orgsIds;
    }
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
    }   //RETOURNE UNE LISTE DE LATLON
    private static int secteur_select(String secteur){
        if (secteur.equals("Public")){return 1;}
        else if(secteur.equals("Prive")){return 2;}
        else {return 0;}
    }



    public static boolean is_email_exist(Context context,String email) throws Exception {
        return !getJsonObjectBykey(context,"email",email,laureats).isNull("id");
    }
    public static boolean is_password_correct(Context context,String email,String password) throws Exception {
        return getJsonObjectBykey(context,"email",email,laureats).getString("password").equals(password);
    }
    public static void logout(Context context){
        setPref(context,-1);
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
    public static Bitmap resize_drawable(Drawable drawable){
        assert drawable != null;
        Bitmap b = ((BitmapDrawable)drawable).getBitmap();
        int width = drawable.getIntrinsicWidth(),heigh=drawable.getIntrinsicHeight();
        float scaleFactor =(float)200/Math.max(heigh,width);
        int sizeX = Math.round(width * scaleFactor);
        int sizeY =  Math.round(heigh* scaleFactor);
        return Bitmap.createScaledBitmap(b, sizeX, sizeY, false);
    }
    public static Bitmap resize_bitmap(Bitmap bitmap){
        int width = bitmap.getWidth(),heigh=bitmap.getHeight();
        float scaleFactor =(float)200/Math.max(heigh,width);
        int sizeX = Math.round(width * scaleFactor);
        int sizeY =  Math.round(heigh* scaleFactor);
        return Bitmap.createScaledBitmap(bitmap, sizeX, sizeY, false);
    }
    public static Bitmap resize_icon(Bitmap bitmap){
        int width = bitmap.getWidth(),heigh=bitmap.getHeight();
        float scaleFactor =(float)100/Math.max(heigh,width);
        int sizeX = Math.round(width * scaleFactor);
        int sizeY =  Math.round(heigh* scaleFactor);
        return Bitmap.createScaledBitmap(bitmap, sizeX, sizeY, false);
    }
    public static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public static Bitmap base64toImage(final String imageString){
        Bitmap new_bitmap;
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        new_bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return new_bitmap;
    }
    public static void set_filter_pref(Context context,String secteur,String promo,long filieree,long org){
        SharedPreferences sharedPreferences = context.getSharedPreferences("filter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sector",secteur);
        editor.putString("promotion",promo);
        editor.putLong("branch",filieree);
        editor.putLong("organisation",org);
        editor.apply();
    }
    public static long get_filter_pref_long(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("filter", Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key,0);
    }
    public static String get_filter_pref_string(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("filter", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"TOUT");
    }
    public static int getPref(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("id",-1);
    }
    public static void setPref(Context context,int user_id){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id",user_id);editor.apply();
    }
    public static void set_pref(Context context,String cle,long id){
        SharedPreferences sharedPreferences = context.getSharedPreferences(cle, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("id",id);
        editor.apply();
    }
    public static long get_pref(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sharedPreferences.getLong("id",0);
    }
    public static JSONObject post_objct_connect(Context context,JSONObject jsonObject,String url){
        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(context);
        result = new JSONObject();volleyError= new VolleyError();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        result = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyError=error;
            }
        });
        ExampleRequestQueue.add(jsonObjectRequest);
        return result;
    }
    public static JSONArray  post_Array_connect(Context context,JSONObject jsonObject,String url){
        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(context);
        JSONArray jsonArray=new JSONArray();jsonArray.put(jsonObject);
        result = new JSONObject();volleyError= new VolleyError();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                resultat=response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyError = error;
            }
        });
        ExampleRequestQueue.add(jsonArrayRequest);
        return resultat;
    }
    public static JSONArray get_Array_connect(Context context,String url){
        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(context);
        result = new JSONObject();volleyError= new VolleyError();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                resultat=response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyError = error;
            }
        });
        ExampleRequestQueue.add(jsonArrayRequest);
        return resultat;
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
    }*/
}
