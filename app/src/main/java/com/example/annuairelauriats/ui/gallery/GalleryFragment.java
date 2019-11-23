package com.example.annuairelauriats.ui.gallery;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.annuairelauriats.R;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GalleryFragment extends Fragment{
    public static ArrayList<Laureat> Laureats;ListView malist;
    private Dialog myDialog,dialogFilter;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        myDialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialogFilter=new Dialog(getActivity());

        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        malist = root.findViewById(R.id.list_laureat);
        peupler_array_list("ALL");
        malist.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Laureat C ;
                        C= Laureats.get(position);
                        ShowPopup(1,1);
                    }
                }
        );
        FloatingActionButton filter_fab = root.findViewById(R.id.fab_filter_laureat);
        filter_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupfilter();
            }
        });


        return root;
    }
    private void ShowPopup(double lon,double lat) {
        myDialog.setContentView(R.layout.laureat_show_position_pop_up);

        myDialog.show();
        myDialog.setOnDismissListener(new AppCompatDialogFragment());
    }
    private void ShowPopupfilter() {
        dialogFilter.setContentView(R.layout.filter_pop_up_liste);
        final Spinner findbyfiliere = dialogFilter.findViewById(R.id.snipper_filtre_laureat_filiere);
        final Spinner findbypromotion = dialogFilter.findViewById(R.id.snipper_filtre_laureat_promotion);
        final Spinner findbyprovince = dialogFilter.findViewById(R.id.snipper_filtre_laureat_province);
        List<String> filieres = peupler_list("filiere");
        List<String> promotions = peupler_list("promotion");
        List<String> provinces = new ArrayList();provinces.add("province1");provinces.add("province 2");provinces.add("province 3");provinces.add("province 4");
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
                        peupler_array_list(findbyfiliere.getSelectedItem().toString());
                        dialogFilter.dismiss();
                    }
                }
        );
        dialogFilter.setCancelable(false);
        dialogFilter.show();
    }
    
    private void peupler_array_list(String filiere){
        Laureats= new ArrayList<>();
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset("myjson.json"));
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                if (filiere.equals("ALL")){

                    Laureats.add(
                            new Laureat(jo_inside.getString("image")+"",
                                    jo_inside.getString("nom")+" "+jo_inside.getString("prenom"),
                                    jo_inside.getString("organisme")+"", jo_inside.getString("email")+""));
                }
                else{
                    if (jo_inside.getString("filiere").equals(filiere)){
                        Laureats.add(
                                new Laureat(jo_inside.getString("image")+"",
                                        jo_inside.getString("nom")+" "+jo_inside.getString("prenom"),
                                        jo_inside.getString("organisme")+"", jo_inside.getString("email")+""));
                    }
                }
            }

            LaureatAdapter adaptateur = new LaureatAdapter(getContext(), Laureats);
            malist.setAdapter(adaptateur);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> peupler_list(String Champ){
        List<String> list_a_peupler = new ArrayList<>();
        list_a_peupler.add("ALL");
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset("myjson.json"));
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
}
