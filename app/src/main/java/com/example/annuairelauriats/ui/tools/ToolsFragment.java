package com.example.annuairelauriats.ui.tools;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.annuairelauriats.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import static com.example.annuairelauriats.ui.home.Classtest.base64toImage;

public class ToolsFragment extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    private LatLng latLng;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    private TextView email ;
    private TextView tel ;
    private float zoom ;
    private LatLng latLng_currennt;
    private Button top, bottom, right,left;private GoogleMap gmap;
    /**
 private CircleImageView pdp_visit;
    private TextView NomPrenomUser ;
    private TextView promotion ;
    private TextView filiere ;
    private TextView organisation ;*/
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = root.findViewById(R.id.map_visit) ;
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        CircleImageView pdp_visit = root.findViewById(R.id.pdp_visit);
        TextView NomPrenomUser = root.findViewById(R.id.nom_visit);
        email = root.findViewById(R.id.email_visit);
        tel = root.findViewById(R.id.tel_visit);
        TextView promotion = root.findViewById(R.id.promotion_visit);
        TextView filiere = root.findViewById(R.id.filiere_visit);
        TextView organisation = root.findViewById(R.id.organisation_visit);
        top=root.findViewById(R.id.go_top_2);bottom=root.findViewById(R.id.go_bottom_2);
        right=root.findViewById(R.id.go_right_2);left=root.findViewById(R.id.go_left_2);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email.getText().toString(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Annuaire Laureats");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Bonjour");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = tel.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });
        email.setTextColor(getResources().getColor(R.color.email));
        tel.setTextColor(getResources().getColor(R.color.email));
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
        gmap.setPadding(1, 1, 1, 1);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
        gmap.addMarker(markerOptions);
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
}