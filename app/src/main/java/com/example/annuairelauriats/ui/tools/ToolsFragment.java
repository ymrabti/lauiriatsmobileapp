package com.example.annuairelauriats.ui.tools;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
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
import de.hdodenhof.circleimageview.CircleImageView;
import static com.example.annuairelauriats.ui.home.Classtest.base64toImage;
import static com.example.annuairelauriats.ui.home.Classtest.filiers;
import static com.example.annuairelauriats.ui.home.Classtest.getJsonObjectBycle;
import static com.example.annuairelauriats.ui.home.Classtest.id_selected;
import static com.example.annuairelauriats.ui.home.Classtest.images_file;
import static com.example.annuairelauriats.ui.home.Classtest.laureats;
import static com.example.annuairelauriats.ui.home.Classtest.org_laureat;
import static com.example.annuairelauriats.ui.home.Classtest.organismes;

public class ToolsFragment extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    /**/@Override public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView = root.findViewById(R.id.map_visit) ;
        mapView.getMapAsync(this);

        CircleImageView pdp_visit = root.findViewById(R.id.pdp_visit);
        TextView NomPrenomUser = root.findViewById(R.id.nom_visit);
        TextView email = root.findViewById(R.id.email_visit);
        TextView tel = root.findViewById(R.id.tel_visit);
        TextView promotion = root.findViewById(R.id.promotion_visit);
        TextView filiere = root.findViewById(R.id.filiere_visit);
        TextView organisation = root.findViewById(R.id.organisation_visit);
        try {
            JSONObject laurat_visitee = getJsonObjectBycle(getActivity(),"id",id_selected,laureats);
            JSONObject image= getJsonObjectBycle(getActivity(),"laureat",id_selected,images_file);
            JSONObject filier= getJsonObjectBycle(getActivity(),"id",laurat_visitee.getInt("filiere"),filiers);
            JSONObject org_lau= getJsonObjectBycle(getActivity(),"id_laureat",id_selected,org_laureat);
            JSONObject org= getJsonObjectBycle(getActivity(),"id",org_lau.getInt("id_org"),organismes);
            pdp_visit.setImageBitmap(base64toImage(image.getString("image")));
            NomPrenomUser.setText(laurat_visitee.getString("nom"));NomPrenomUser.append(" ");NomPrenomUser.append(laurat_visitee.getString("prenom"));
            email.setText(laurat_visitee.getString("email"));
            tel.setText(laurat_visitee.getString("telephone"));
            promotion.setText(laurat_visitee.getString("promotion"));
            filiere.setText(filier.getString("Nom"));
            organisation.setText(org.getString("org"));
            LatLng latLng = new LatLng(org.getDouble("latitude"), -org.getDouble("longitude"));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            gmap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            gmap.addMarker(markerOptions);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(1);
        gmap.setIndoorEnabled(true);
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}