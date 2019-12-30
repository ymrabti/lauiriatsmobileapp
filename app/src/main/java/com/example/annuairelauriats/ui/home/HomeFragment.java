package com.example.annuairelauriats.ui.home;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.aide.HelpFragment;
import com.example.annuairelauriats.ui.signaler.SignalerFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.annuairelauriats.MainActivity.navigationView;
import static com.example.annuairelauriats.ui.home.Classtest.base64toImage;
import static com.example.annuairelauriats.ui.home.Classtest.connect_to_backend_array;
import static com.example.annuairelauriats.ui.home.Classtest.email_connected;
import static com.example.annuairelauriats.ui.home.Classtest.ip_server;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView mapView;private GoogleMap gmap;
    private float zoom ;
    private LatLng latLng_currennt;
    private Button top, bottom, right,left;
    private static FragmentTransaction fragmentTransaction;
    private CircleImageView pdp_visit ;
    private TextView email ;
    private TextView tel ;public static int statut;
    private TextView promotion ;private List<etapes_parcours> list_parcours;
    private TextView filiere ;private Dialog dialog;private LinearLayout linearLayout;
    private TextView organisation ,status_profile;private ScrollView scrollView;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        setHasOptionsMenu(true);
        assert getFragmentManager() != null;
        fragmentTransaction = getFragmentManager().beginTransaction();
        scrollView= root.findViewById(R.id.scroll_home);
        scrollView.setVisibility(View.GONE);
        mapView = root.findViewById(R.id.map_laureat_profile_sssss) ;
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        pdp_visit = root.findViewById(R.id.pdp_laureat_profile_sssss);
        email = root.findViewById(R.id.email_laureat_profile_sssss);
        tel = root.findViewById(R.id.tel_laureat_profile_sssss);
        promotion = root.findViewById(R.id.promotion_laureat_profile_sssss);
        filiere = root.findViewById(R.id.filiere_laureat_profile_sssss);
        organisation = root.findViewById(R.id.organisation_laureat_profile_sssss);
        status_profile= root.findViewById(R.id.status_profile);
        linearLayout = root.findViewById(R.id.status_profile_linear);
        parcours_pro_data();
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.popup_wait);
        dialog.setCancelable(false);
        dialog.show();

        final Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try
                {
                    scrollView.setVisibility(View.VISIBLE);
                    JSONObject laureat = response.getJSONObject(0);
                    pdp_visit.setImageBitmap(base64toImage(laureat.getString("photo")));
                    String nom_complet=laureat.getString("Nom")+" "+laureat.getString("Prenom");
                    Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar())
                            .setTitle(nom_complet);
                    email.setText(laureat.getString("email"));

                    View headerLayout = navigationView.getHeaderView(0);
                    CircleImageView image_main = headerLayout.findViewById(R.id.pdp_show);
                    TextView nom_prenom = headerLayout.findViewById(R.id.usernameclc);
                    TextView email = headerLayout.findViewById(R.id.email_top_navheader);

                    image_main.setImageBitmap(base64toImage(laureat.getString("photo")));
                    nom_prenom.setText(nom_complet);email.setText(email_connected);

                    tel.setText(laureat.getString("Telephone"));
                    promotion.setText(laureat.getInt("Promotion")+"");
                    filiere.setText(laureat.getString("Nom_filiere"));
                    statut=laureat.getInt("id_lesstatus");
                    Menu menuNav=navigationView.getMenu();
                    MenuItem nav_slideshow = menuNav.findItem(R.id.nav_slideshow);
                    MenuItem nav_gallery = menuNav.findItem(R.id.nav_gallery);
                    if (statut==4){
                        nav_slideshow.setVisible(true);nav_gallery.setVisible(true);
                    }
                    statusise(laureat.getString("status"),laureat.getString("motif"));
                    if (laureat.getInt("org")==0){
                        orgnaisation_attente();
                    }
                    else{
                        orgnaisation_(laureat.getInt("org"));
                    }
                    for (int i= 0;i<list_parcours.size();i++){
                        etapes_parcours actuelle = list_parcours.get(i);
                        if (actuelle.getType()==0){
                            parcours(Color.argb(100,2,255,200)
                                    ,actuelle.getDate_debut(),actuelle.getOrganisme(),actuelle.getFonction());
                        }
                        else if (actuelle.getType()==1){
                            parcours(Color.argb(100,187,83,84)
                                    ,actuelle.getDate_debut(),actuelle.getOrganisme(),actuelle.getFonction());
                        }
                    }
                    dialog.dismiss();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };
        connect_to_backend_array(getContext(),Request.Method.GET, "/laureat/id/"+email_connected,
                null, listener, errorListener);

        top=root.findViewById(R.id.go_top);bottom=root.findViewById(R.id.go_bottom);
        right=root.findViewById(R.id.go_right);left=root.findViewById(R.id.go_left);

        return root;
    }
    private void orgnaisation_(int _id_org)  {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject laureat = response.getJSONObject(0);
                    organisation.setText(laureat.getString("Nom"));
                    LatLng latLng = new LatLng(laureat.getDouble("Latitude"),laureat.getDouble("Longitude"));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
                    gmap.addMarker(markerOptions);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"org  "+e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, ip_server + "/laureat/org/"+_id_org,
                null, listener, errorListener);
        requestQueue.add(jsonArrayRequest);
    }
    private void orgnaisation_attente()  {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject laureat = response.getJSONObject(0);
                    organisation.setText(laureat.getString("Nom"));
                    LatLng latLng = new LatLng(laureat.getDouble("Latitude"),laureat.getDouble("Longitude"));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
                    gmap.addMarker(markerOptions);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"attente "+e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, ip_server + "/autres/org/"+email_connected,
                null, listener, errorListener);
        requestQueue.add(jsonArrayRequest);
    }
    private void parcours(int color,String date_debut,String nom,String intitule){
        LinearLayout parcours_prof = getView().findViewById(R.id.parcours_professionnelle);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        parcours_prof.setLayoutParams(layoutParams);
        parcours_prof.setGravity(Gravity.CENTER);

        TextView textView = new TextView (getContext());
        textView.setTextSize(18);
        textView.setLayoutParams(layoutParams);
        textView.setText(Html.fromHtml(date_debut));textView.append("     ");
        textView.append(nom+"\n");textView.append(intitule);
        textView.setPadding(10,10,10,10);
        textView.setBackgroundColor(color);
        textView.setTextColor(Color.argb(100,88,88,88));
        parcours_prof.addView(textView );
    }
    @Override public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override public void onResume() { super.onResume();mapView.onResume(); }
    @Override public void onStart() { super.onStart();mapView.onStart(); }
    @Override public void onStop() { super.onStop();mapView.onStop(); }
    @Override public void onPause() { mapView.onPause();super.onPause(); }
    @Override public void onDestroy() { mapView.onDestroy();super.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory();mapView.onLowMemory(); }
    @Override public void onMapReady(GoogleMap googleMap) {
        gmap=googleMap;
        gmap.setMinZoomPreference(1);
        gmap.setIndoorEnabled(true);
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latLng_currennt = gmap.getCameraPosition().target;
                zoom = gmap.getCameraPosition().zoom;
                LatLng farLeft = gmap.getProjection().getVisibleRegion().farLeft;
                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng_currennt.latitude,farLeft.longitude), zoom));
            }
        });
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latLng_currennt = gmap.getCameraPosition().target;
                zoom = gmap.getCameraPosition().zoom;
                LatLng farLeft = gmap.getProjection().getVisibleRegion().farLeft;
                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(farLeft.latitude,latLng_currennt.longitude), zoom));
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latLng_currennt = gmap.getCameraPosition().target;
                zoom = gmap.getCameraPosition().zoom;
                LatLng droit_bas = gmap.getProjection().getVisibleRegion().nearRight;
                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng_currennt.latitude,droit_bas.longitude), zoom));
            }
        });
        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latLng_currennt = gmap.getCameraPosition().target;
                zoom = gmap.getCameraPosition().zoom;
                LatLng droit_bas = gmap.getProjection().getVisibleRegion().nearRight;
                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(droit_bas.latitude,latLng_currennt.longitude), zoom));
            }
        });
    }
    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_edit_profile).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }
    public static void edit_profile(){
        fragmentTransaction.replace(R.id.nav_host_fragment, new HelpFragment());
        fragmentTransaction.replace(R.id.nav_host_fragment, new SignalerFragment());
        fragmentTransaction.commit();
    }
    private void statusise(String status,String motif){
        status_profile.append(status);status_profile.append("\n");
        if (statut==1){
            linearLayout.setBackgroundColor(Color.argb(100,75,200,75));
            status_profile.append("votre demande de joindre notre communaute est en cours de traitement");
        }
        else if (statut==2){
            linearLayout.setBackgroundColor(Color.RED);
            status_profile.append(motif+"\n");
            status_profile.append("nous vous invitons a modifier vos information pour une autre reinscription");
        }
        else if (statut==3){
            linearLayout.setBackgroundColor(Color.argb(100,255,75,60));
            status_profile.append(motif+"\n");
            status_profile.append("votre demande en cours de traitement");
        }
        else if (statut==4){
            linearLayout.setBackgroundColor(Color.GREEN);
        }
    }
    private void parcours_pro_data(){
        list_parcours = new ArrayList<>();
        connect_to_backend_array(getContext(), Request.Method.GET
                , "/autres/orgs_laureat/" + email_connected, null
                , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length()!=0){
                                for (int i=0;i<response.length();i++){
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    list_parcours.add(new etapes_parcours(
                                            jsonObject.getString("date_debut").substring(0,10)
                                            ,jsonObject.getString("Nom")
                                            ,jsonObject.getString("fonction"),0
                                    ));
                                }
                            }
                        }
                        catch (Exception e){e.printStackTrace();}

                    }
                },null );
        connect_to_backend_array(getContext(), Request.Method.GET
                , "/autres/org/" + email_connected, null
                , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length()!=0){
                                for (int i=0;i<response.length();i++){
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    list_parcours.add(new etapes_parcours(
                                            jsonObject.getString("date_debut").substring(0,10)
                                            ,jsonObject.getString("Nom")
                                            ,jsonObject.getString("intitule"),1
                                    ));
                                }
                            }
                        }
                        catch (Exception e){e.printStackTrace();}

                    }
                },null );
    }
    private class etapes_parcours{
        private String date_debut,Organisme,fonction;int type;
        etapes_parcours(String date_debut,String organisme,String fonction,int type){
            this.date_debut=date_debut;this .Organisme=organisme;this.fonction=fonction;this.type=type;
        }

        private String getDate_debut() {
            return date_debut;
        }

        private String getFonction() {
            return fonction;
        }

        private String getOrganisme() {
            return Organisme;
        }

        public int getType() {
            return type;
        }
    }

}