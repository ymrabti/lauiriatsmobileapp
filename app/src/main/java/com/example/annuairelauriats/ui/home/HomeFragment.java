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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.annuairelauriats.ui.home.Classtest.base64toImage;
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
    private TextView NomPrenomUser;
    private TextView email ;
    private TextView tel ;
    private TextView promotion ;
    private TextView filiere ;private Dialog dialog;private LinearLayout linearLayout;
    private TextView organisation ,status_profile;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        setHasOptionsMenu(true);
        assert getFragmentManager() != null;
        fragmentTransaction = getFragmentManager().beginTransaction();
        mapView = root.findViewById(R.id.map_laureat_profile_sssss) ;
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        pdp_visit = root.findViewById(R.id.pdp_laureat_profile_sssss);
        NomPrenomUser = root.findViewById(R.id.nom_laureat_profile_sssss);
        email = root.findViewById(R.id.email_laureat_profile_sssss);
        tel = root.findViewById(R.id.tel_laureat_profile_sssss);
        promotion = root.findViewById(R.id.promotion_laureat_profile_sssss);
        filiere = root.findViewById(R.id.filiere_laureat_profile_sssss);
        organisation = root.findViewById(R.id.organisation_laureat_profile_sssss);
        status_profile= root.findViewById(R.id.status_profile);
        linearLayout = root.findViewById(R.id.status_profile_linear);
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.popup_wait);
        dialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try
                {
                    JSONObject laureat = response.getJSONObject(0);
                    pdp_visit.setImageBitmap(base64toImage(laureat.getString("photo")));
                    String nom_complet=laureat.getString("Nom")+" "+laureat.getString("Prenom");
                    NomPrenomUser.setText(nom_complet);
                    Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar())
                            .setTitle(nom_complet);
                    email.setText(laureat.getString("email"));
                    tel.setText(laureat.getString("Telephone"));
                    promotion.setText(laureat.getInt("Promotion")+"");
                    filiere.setText(laureat.getString("Nom_filiere"));
                    organisation.setText(laureat.getString("nom_org"));
                    LatLng latLng = new LatLng(laureat.getDouble("Latitude"),laureat.getDouble("Longitude"));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
                    gmap.addMarker(markerOptions);
                    status_profile.append(laureat.getString("status"));status_profile.append("\n");
                    status_profile.append("motif : ");status_profile.append(laureat.getString("motif"));
                    if (laureat.getInt("id_lesstatus")==1){
                        linearLayout.setBackground(getActivity().getDrawable(R.drawable.colors_laureat_item));
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
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, ip_server + "/laureat/id/"+email_connected,
                null, listener, errorListener);
        requestQueue.add(jsonArrayRequest);

        top=root.findViewById(R.id.go_top);bottom=root.findViewById(R.id.go_bottom);
        right=root.findViewById(R.id.go_right);left=root.findViewById(R.id.go_left);

        return root;
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
}