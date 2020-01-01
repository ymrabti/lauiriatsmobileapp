package com.example.annuairelauriats.ui.home;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.example.annuairelauriats.ui.register.RegisterActivity;
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
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.example.annuairelauriats.ui.gallery.GalleryFragment.imageView_nodata;
import static com.example.annuairelauriats.ui.gallery.GalleryFragment.laureats_list;
import static java.lang.Math.min;

public class Classtest  extends AppCompatActivity {
    public static int portBackend=3000;
    public static String ip_serverIP="192.168.137.1",ip_server="http://"+ip_serverIP+":"+portBackend,email_connected;

    public static Dialog dialog_universelle;
    public static String sql="SELECT laureats.*,filieres.Nom as Nom_filiere,les_status.nom as status" +
            ",motif,les_status.id_lesstatus,organisme.secteur,organisme.province" +
            " ,organisme.Latitude,organisme.Longitude,organisme.Nom as NomOrg"
            +" FROM  laureats,filieres,les_status ,laureat_statut,organisme"
            +" WHERE laureats.Filiere=filieres.id_filieres and laureats.org=organisme.id_org"
            +" and laureats.email=laureat_statut.id_laureat and laureat_statut.id_statut=les_status.id_lesstatus "
    ,additional_sql="",email_selected;
    public static int shared_org;
    @SuppressLint("StaticFieldLeak")
    public static void promotion_peuplement(Context context,int premier,Spinner spinner) throws Exception {
        ArrayList<String> promos_filier = new ArrayList<>();promos_filier.add("SELECTIONNER");
        Calendar rightNow = Calendar.getInstance();
        if (premier!=0){
            for (int i=premier;i<=rightNow.get(Calendar.YEAR)+1;i++){
                promos_filier.add(i+"");
            }
        }
        ArrayAdapter<String> list_adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,promos_filier);
        list_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(list_adapter);
    }
    public static void ShowPopupfilter(final Context context, final ListView listView, final GoogleMap googleMap, final int mark) {
        final Dialog dialogFilter = new Dialog(context);
        dialogFilter.setContentView(R.layout.filter_pop_up_liste);
        final int[] filiere_selected = new int[1];
        final int[] organisation_selected = new int[1];


        final Spinner findbyfiliere = dialogFilter.findViewById(R.id.snipper_filtre_laureat_filiere);
        final Spinner findbypromotion = dialogFilter.findViewById(R.id.snipper_filtre_laureat_promotion);
        final Spinner findbyprovince = dialogFilter.findViewById(R.id.snipper_filtre_laureat_province);
        final Spinner findbyorganisation = dialogFilter.findViewById(R.id.snipper_filtre_laureat_organisation);
        final Spinner findbysecteur = dialogFilter.findViewById(R.id.snipper_filtre_laureat_secteur);


        List<String> secteurs = new ArrayList<>();
        secteurs.add("SELECTIONNER");secteurs.add("public");secteurs.add("prive");
        ArrayAdapter<String> list_org_adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,secteurs);
        list_org_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        findbysecteur.setAdapter(list_org_adapter);
        final List<Filiere> dates ;final List<Integer> organs;

        dates = new ArrayList<>();organs = new ArrayList<>();
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<String> filieres = new ArrayList<>();filieres.add("SELECTIONNER");
                for (int i=0;i<response.length();i++){
                    try {
                        JSONObject filiere_actuelle= response.getJSONObject(i);
                        filieres.add(filiere_actuelle.getString("Nom"));
                        dates.add(new Filiere(Integer.parseInt(
                                filiere_actuelle.getString("Date_Creation").substring(0,4).trim())
                                ,filiere_actuelle.getInt("id_filieres")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ArrayAdapter<String> list_adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,filieres);
                list_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                findbyfiliere.setAdapter(list_adapter);
            }
        };
        connect_to_backend_array(context, Request.Method.GET,"/autres/filieres",
                null, listener, null);

        Response.Listener<JSONArray> listener_org = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<String> organismes = new ArrayList<>();organismes.add("SELECTIONNER");
                for (int i=0;i<response.length();i++){
                    try {
                        JSONObject organisme_actuelle= response.getJSONObject(i);
                        organismes.add(organisme_actuelle.getString("Nom"));
                        organs.add(organisme_actuelle.getInt("id_org"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ArrayAdapter<String> list_adapter =
                        new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,organismes);
                list_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                findbyorganisation.setAdapter(list_adapter);
            }
        };
        connect_to_backend_array(context,Request.Method.GET,  "/autres/organismes",
                null, listener_org, null);

        Response.Listener<JSONArray> province_listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<String> provinces_names = new ArrayList<>();provinces_names.add("SELECTIONNER");
                for (int i=0;i<response.length();i++){
                    try {
                        JSONObject organisme_actuelle= response.getJSONObject(i);
                        provinces_names.add(organisme_actuelle.getString("provicne"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ArrayAdapter<String> list_adapter =
                        new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,provinces_names);
                list_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                findbyprovince.setAdapter(list_adapter);
            }
        };
        connect_to_backend_array(context,Request.Method.GET,  "/autres/provinces",
                null, province_listener, null);
        try {

            promotion_peuplement(context, 1973, findbypromotion);
        }
        catch (Exception exc){exc.printStackTrace();}



        findbyfiliere.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {if (position==0){
                    promotion_peuplement(context, 1973, findbypromotion);
                }
                else {
                    filiere_selected[0] =dates.get(position-1).get_Id();
                    promotion_peuplement(context, dates.get(position-1).getPrimier_promo(), findbypromotion);
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        dialogFilter.findViewById(R.id.button_dismiss_filter).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { dialogFilter.dismiss(); }
                }
            );


        findbyorganisation.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener()
                {
                    @Override public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                    {
                        if (position!=0){
                            organisation_selected[0] =organs.get(position-1);
                            findbysecteur.setEnabled(false);findbyprovince.setEnabled(false);
                        }
                        else {findbysecteur.setEnabled(true);findbyprovince.setEnabled(true);
                            organisation_selected[0] =0;}
                    }
                    @Override public void onNothingSelected(AdapterView<?> parentView) {}
                }
            );
        findbysecteur.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                if (position==0 && findbyprovince.getSelectedItemId()==0){findbyorganisation.setEnabled(true);}
                else{findbyorganisation.setEnabled(false);}
            }
            @Override public void onNothingSelected(AdapterView<?> parentView) {}
                }
            );
        findbyprovince.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                    {
                        if (position==0 && findbysecteur.getSelectedItemId()==0){findbyorganisation.setEnabled(true);}
                        else{findbyorganisation.setEnabled(false);}
                    }
                    @Override public void onNothingSelected(AdapterView<?> parentView) {}
                }
        );


        dialogFilter.findViewById(R.id.button_save_filter).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringBuilder stringBuilder= new StringBuilder();
                        StringBuilder stringBuilder1= new StringBuilder();stringBuilder1.append(" ");
                        stringBuilder.append(sql);
                        if (!findbypromotion.getSelectedItem().equals("SELECTIONNER")){
                                stringBuilder.append(" and Promotion= ").append(findbypromotion.getSelectedItem());
                                stringBuilder1.append(" and Promotion= ").append(findbypromotion.getSelectedItem());
                        }
                        if (!findbyfiliere.getSelectedItem().equals("SELECTIONNER")){
                            stringBuilder.append("   and Filiere=  ").append(filiere_selected[0]);
                            stringBuilder1.append("   and Filiere=  ").append(filiere_selected[0]);
                        }
                        if (!findbysecteur.getSelectedItem().equals("SELECTIONNER")){
                            stringBuilder.append(" and secteur= ").append("\"").append(findbysecteur.getSelectedItem()).append("\"");
                            stringBuilder1.append(" and secteur= ").append("\"").append(findbysecteur.getSelectedItem()).append("\"");
                        }
                        if (!findbyprovince.getSelectedItem().equals("SELECTIONNER")){
                            stringBuilder.append("   and province=  ").append("\"")
                                    .append(findbyprovince.getSelectedItem()).append("\"");
                            stringBuilder1.append("   and province=  ").append("\"")
                                    .append(findbyprovince.getSelectedItem()).append("\"");
                        }
                        if (!findbyorganisation.getSelectedItem().equals("SELECTIONNER")){
                            stringBuilder.append("   and org =  ").append(organisation_selected[0]);
                            shared_org=organisation_selected[0];
                        }
                        additional_sql=stringBuilder1.toString();
                        if (mark==1){
                            show_laureats_on_list(context,stringBuilder.toString(),listView);
                        }
                        if (mark==0){
                            show_laureats_on_map(context,stringBuilder.toString(),googleMap);
                        }
                        set_filter_pref(context,findbysecteur.getSelectedItemId()
                                ,findbyprovince.getSelectedItemId(),findbyorganisation.getSelectedItemId()
                                ,findbyfiliere.getSelectedItemId(),findbypromotion.getSelectedItemId());

                        dialogFilter.dismiss();
                    }
                }
        );
        dialogFilter.setCancelable(false);


        CountDownTimer countDownTimer = new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                long position_org = get_filter_pref_long(context,"organisation")
                        , position_secteur=get_filter_pref_long(context,"sector")
                        ,position_province=get_filter_pref_long(context,"province")
                        ,position_filiere=get_filter_pref_long(context,"branch")
                        ,position_promotion=get_filter_pref_long(context,"promotion");
//setclipboard(position_filiere+"  "+position_org+"   "+position_promotion+"  "+position_secteur+"  "+position_province,context);

                if (position_org!=0){
                    findbyorganisation.setSelection((int)position_org);
                }
                else {
                    findbysecteur.setSelection((int)position_secteur);findbyprovince.setSelection((int)position_province);
                }
                findbyfiliere.setSelection((int)position_filiere);findbypromotion.setSelection((int)position_promotion);
            }
        };countDownTimer.start();
        dialogFilter.show();
    }
    public static void show_laureats_on_list(final Context context, String sql_parameter, final ListView listView){
        dialog_universelle = new Dialog(context);
        dialog_universelle.setContentView(R.layout.popup_wait);
        dialog_universelle.setCancelable(false);
        dialog_universelle.show();
        connect_to_backend_array(context, Request.Method.GET, "/autres/requestAny/" + sql_parameter
                , null
                , new Response.Listener<JSONArray>() {
@Override
public void onResponse(JSONArray response) {
    laureats_list = new ArrayList<>();
    if (response.length()!=0){
        imageView_nodata.setVisibility(View.GONE);
        try {
            for (int i=0;i<response.length();i++){
                JSONObject laureat_courant = response.getJSONObject(i);
                Laureat laureat_to_add = new Laureat(laureat_courant.getString("email")+""
                        ,laureat_courant.getString("photo")+" ",
                        laureat_courant.getString("Nom")+" "+laureat_courant.getString("Prenom"),
                        laureat_courant.getString("NomOrg")+"", laureat_courant.getString("Description")+"");
                laureats_list.add(laureat_to_add);
            }
        }
        catch (Exception er){
            setclipboard("error :"+er.toString(),context);
            Toast.makeText(context,er.toString()+"\n"+er.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    else{
        imageView_nodata.setVisibility(View.VISIBLE);
    }
    LaureatAdapter adaptateur = new LaureatAdapter(context, laureats_list);
    listView.setAdapter(adaptateur);
    dialog_universelle.dismiss();
}
}, new Response.ErrorListener() {
@Override
public void onErrorResponse(VolleyError error) {
    setclipboard("error :"+error.toString(),context);
}
});
    }
    public static void show_laureats_on_map(final Context context, String sql_parameter, final GoogleMap gmap){
        String new_sql = "SELECT org,Latitude,Longitude,count(*) as Nombre from ("+sql_parameter+") AS laureats_selection " +
                "GROUP BY org,Latitude,Longitude";
        dialog_universelle = new Dialog(context);
        dialog_universelle.setContentView(R.layout.popup_wait);
        dialog_universelle.setCancelable(false);
        dialog_universelle.show();
        connect_to_backend_array(context, Request.Method.GET, "/autres/requestAny/" + new_sql
                , null
                , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        gmap.clear();
                        if (response.length()!=0){
                            try {
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for (int i=0;i<response.length();i++){
                                    JSONObject organisation_courant = response.getJSONObject(i);

                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(new LatLng(organisation_courant.getDouble("Latitude")
                                            ,organisation_courant.getDouble("Longitude")));
                                    markerOptions.title(""+organisation_courant.getInt("org"));
                                    Bitmap icoon = getRoundedShape(get_Bitmap(context,R.drawable.circle_shape));
                                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(add_text(icoon,
                                            organisation_courant.getInt("Nombre")+"")));
                                    builder.include(new LatLng(organisation_courant.getDouble("Latitude")
                                            ,organisation_courant.getDouble("Longitude")));
                                    markerOptions.flat(true);
                                    gmap.addMarker(markerOptions);
                                }
                                LatLngBounds bounds = builder.build();
                                int padding = 50;
                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                gmap.moveCamera(cu);gmap.animateCamera(cu);
                            }
                            catch (Exception er){
                                setclipboard("error :"+er.toString(),context);
                                Toast.makeText(context,er.toString()+"\n"+er.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                        dialog_universelle.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setclipboard("error :"+error.toString(),context);
                    }
                });
    }
    private static int secteur_select(String secteur){
        if (secteur.equals("Public")){return 1;}
        else if(secteur.equals("Prive")){return 2;}
        else {return 0;}
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
    public static void set_filter_pref(Context context,long sector,long province,long org,long branch,long promotion){
        SharedPreferences sharedPreferences = context.getSharedPreferences("filter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("sector",sector);editor.putLong("province",province);
        editor.putLong("organisation",org);
        editor.putLong("promotion",promotion);editor.putLong("branch",branch);
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
    public static String get0Pref(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        return sharedPreferences.getString("email","noreply");
    }
    public static void set0Pref(Context context,String user_email){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email",user_email);editor.apply();
    }


    public static void setclipboard(String message,Context context){
        ClipboardManager cManager = (ClipboardManager) Objects.requireNonNull(context).getSystemService(
                Context.CLIPBOARD_SERVICE);
        ClipData cData = ClipData.newPlainText("text", message+"");
        if (cManager != null) {
            cManager.setPrimaryClip(cData);
            Toast.makeText(context,"copied to clipboard",Toast.LENGTH_LONG).show();
        }
    }
    public static void logout(Context context){
        set0Pref(context,"noreply");
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
    public static void connect_to_backend(Context context, int method, String url, JSONObject jsonObject
            , Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                method
                , ip_server + url, jsonObject
                , listener, errorListener);
        requestQueue.add(jsonObjectRequest);
    }
    public static void connect_to_backend_array(Context context, int method, String url, JSONArray jsonArray
            , Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                method
                , ip_server + url, jsonArray
                , listener, errorListener);
        requestQueue.add(jsonArrayRequest);
    }

    @SuppressLint("HardwareIds")
    public static String getUniqueIMEIId() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00";
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
    }

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
