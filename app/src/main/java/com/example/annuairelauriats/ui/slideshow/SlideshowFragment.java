package com.example.annuairelauriats.ui.slideshow;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.gallery.GalleryFragment;
import com.example.annuairelauriats.ui.home.Classtest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.example.annuairelauriats.ui.home.Classtest.filter;
import static com.example.annuairelauriats.ui.home.Classtest.getJsonObjectBycle;
import static com.example.annuairelauriats.ui.home.Classtest.loadJSONFromAsset;
import static com.example.annuairelauriats.ui.home.Classtest.organismes;

public class SlideshowFragment extends Fragment  implements OnMapReadyCallback{
     private MapView mapView;
     private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static long filiere=-1,org=-1;public static String promo="TOUTE",secteur="TOUTE";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = root.findViewById(R.id.map_view) ;
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        FloatingActionButton filter_fab = root.findViewById(R.id.fab_filter_laureat_on_map);
        filter_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Classtest.ShowPopupfilter(getActivity(),null ,gmap,0);
            }
        });
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
        gmap = googleMap;
        gmap.setMinZoomPreference(1);
        LatLng ny = new LatLng(33.589886, -7.603869);
        gmap.setIndoorEnabled(true);
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        if (filiere==-1 && org==-1&& secteur.equals("TOUTE") && promo.equals(secteur) ){
            try {
                JSONObject filter_json = new JSONObject(
                        Objects.requireNonNull(loadJSONFromAsset(
                                Objects.requireNonNull(getActivity()), filter)));
                filiere=filter_json.getInt("filiere");promo=filter_json.getString("promotion");
                org=filter_json.getInt("organisme");secteur=filter_json.getString("secteur");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Classtest.show_laureats_on_map(getActivity(),filiere,promo,"TOUT",org,secteur,gmap);
        }
        else{
            Classtest.show_laureats_on_map(getActivity(),filiere,promo,"TOUT",org,secteur,gmap);
        }
        gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                GalleryFragment.org=Integer.parseInt(marker.getTitle().trim());
                GalleryFragment.filiere=filiere;
                GalleryFragment.promo=promo;
                GalleryFragment.secteur=secteur;
                assert getFragmentManager() != null;
                Fragment fragment = new GalleryFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                MainActivity.navigationView.setCheckedItem(R.id.nav_gallery);
                return true;
            }
        });
        /*gmap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                long id_org = Integer.parseInt(marker.getTitle().trim());
                Toast.makeText(getContext(),"drag start : "+id_org,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                long id_org = Integer.parseInt(marker.getTitle().trim());
                Toast.makeText(getContext(),"drag drag : "+id_org,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                long id_org = Integer.parseInt(marker.getTitle().trim());
                Toast.makeText(getContext(),"drag end : "+id_org,Toast.LENGTH_LONG).show();
            }
        });*/
    }


}