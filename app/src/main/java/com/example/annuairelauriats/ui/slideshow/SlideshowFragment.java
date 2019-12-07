package com.example.annuairelauriats.ui.slideshow;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.aide.HelpFragment;
import com.example.annuairelauriats.ui.gallery.GalleryFragment;
import com.example.annuairelauriats.ui.home.Classtest;
import com.example.annuairelauriats.ui.tools.ToolsFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import static com.example.annuairelauriats.ui.home.Classtest.get_filter_pref_long;
import static com.example.annuairelauriats.ui.home.Classtest.get_filter_pref_string;
import static com.example.annuairelauriats.ui.home.Classtest.id_selected;

public class SlideshowFragment extends Fragment  implements OnMapReadyCallback{
     private MapView mapView;
     private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

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
        gmap.setIndoorEnabled(true);
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        long filiere = get_filter_pref_long(Objects.requireNonNull(getActivity()), "branch");
        String promo = get_filter_pref_string(getActivity(), "promotion");
        long org = get_filter_pref_long(getActivity(), "organisation");
        String secteur = get_filter_pref_string(getActivity(), "sector");
        Classtest.show_laureats_on_map(getActivity(), filiere, promo,"TOUT", org, secteur,gmap);
        gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                assert getFragmentManager() != null;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new HelpFragment());
                if(marker.isFlat()){
                    Fragment fragment = new GalleryFragment();
                    Bundle bundle=new Bundle();
                    bundle.putLong("organisation", Integer.parseInt(marker.getTitle().trim()));
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                    fragmentTransaction.commit();
                    MainActivity.navigationView.setCheckedItem(R.id.nav_gallery);
                }
                else{
                    id_selected=Integer.parseInt(marker.getTitle().trim());
                    fragmentTransaction.replace(R.id.nav_host_fragment, new ToolsFragment());
                    fragmentTransaction.commit();
                }
                return true;
            }
        });
    }


}