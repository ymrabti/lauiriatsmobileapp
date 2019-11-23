package com.example.annuairelauriats.ui.slideshow;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.annuairelauriats.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SlideshowFragment extends Fragment  implements OnMapReadyCallback{
    //private SlideshowViewModel slideshowViewModel;
     private MapView mapView;
     private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private Dialog dialogFilter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel;
        slideshowViewModel = ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        dialogFilter=new Dialog(getActivity());

        mapView = root.findViewById(R.id.map_view) ;
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        FloatingActionButton filter_fab = root.findViewById(R.id.fab_filter_laureat_on_map);
        filter_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupfilter();
            }
        });
        return root;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
    private void ShowPopupfilter() {
        dialogFilter.setContentView(R.layout.filter_pop_up_liste);
        final Spinner findbyfiliere = dialogFilter.findViewById(R.id.snipper_filtre_laureat_filiere);
        final Spinner findbypromotion = dialogFilter.findViewById(R.id.snipper_filtre_laureat_promotion);
        final Spinner findbyprovince = dialogFilter.findViewById(R.id.snipper_filtre_laureat_province);
        List<String> filieres = peupler_list("filiere","filieres.json");
        List<String> promotions = peupler_list("promotion","promotions.json");
        List<String> provinces = peupler_list("province","provinces.json");
        ArrayAdapter filieresadapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,filieres);filieresadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter promotionsadapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,promotions);promotionsadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter provincesadapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,provinces);provincesadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        findbyfiliere.setAdapter(filieresadapter);findbypromotion.setAdapter(promotionsadapter);
        findbyprovince.setAdapter(provincesadapter);
        dialogFilter.findViewById(R.id.button_save_filter).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),
                                "filiere : "+findbyfiliere.getSelectedItem()
                                        +"\npromotion : "+findbypromotion.getSelectedItem()
                                        +"\nprovince "+ findbyprovince.getSelectedItem(),
                                Toast.LENGTH_LONG).show();
                        show_laureats_on_map(findbyfiliere.getSelectedItem().toString());
                        dialogFilter.dismiss();
                    }
                }
        );
        dialogFilter.setCancelable(false);
        dialogFilter.show();
    }

    private void show_laureats_on_map(String filiere){
        List<LatLng> latLngs= peupler_list_latslongs(filiere);
        for(int i=0;i<latLngs.size();i++){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLngs.get(i));
            gmap.addMarker(markerOptions);
        }
    }
    private List<LatLng> peupler_list_latslongs(String filiere){
        List<LatLng> list_a_peupler = new ArrayList<>();
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset("myjson.json"));
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                if (filiere.equals("ALL")){
                    list_a_peupler.add(new LatLng(jo_inside.getDouble("latitude"),jo_inside.getDouble("longitude")));
                }
                else{
                    if (jo_inside.getString("filiere").equals(filiere)){
                        list_a_peupler.add(new LatLng(jo_inside.getDouble("latitude"),jo_inside.getDouble("longitude")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list_a_peupler;
    }

    private List<String> peupler_list(String Champ,String file){
        List<String> list_a_peupler = new ArrayList<>();
        list_a_peupler.add("ALL");
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset(file));
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                list_a_peupler.add(jo_inside.getString(Champ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list_a_peupler;
    }
    private String loadJSONFromAsset(String fichier) {
        String json;
        try {
            File myExternalFile = new File(Objects.requireNonNull(getActivity()).getExternalFilesDir("Annuaire"), fichier);
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
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        //show_laureats_on_map("ALL");
    }

}