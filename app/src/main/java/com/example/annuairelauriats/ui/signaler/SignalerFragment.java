package com.example.annuairelauriats.ui.signaler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.aide.HelpFragment;
import com.example.annuairelauriats.ui.home.Filiere;
import com.example.annuairelauriats.ui.home.HomeFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.example.annuairelauriats.ui.home.Classtest.base64toImage;
import static com.example.annuairelauriats.ui.home.Classtest.connect_to_backend;
import static com.example.annuairelauriats.ui.home.Classtest.connect_to_backend_array;
import static com.example.annuairelauriats.ui.home.Classtest.email_connected;
import static com.example.annuairelauriats.ui.home.Classtest.encodeImage;
import static com.example.annuairelauriats.ui.home.Classtest.promotion_peuplement;
import static com.example.annuairelauriats.ui.home.Classtest.resize_bitmap;
import static com.example.annuairelauriats.ui.home.Classtest.resize_drawable;
import static com.example.annuairelauriats.ui.home.Classtest.set0Pref;
import static com.example.annuairelauriats.ui.home.HomeFragment.Orgedited;
import static com.example.annuairelauriats.ui.home.HomeFragment.daaateee_deeebbuuuu;
import static com.example.annuairelauriats.ui.home.HomeFragment.intiiiitullee;
import static com.example.annuairelauriats.ui.home.HomeFragment.laatloong;
import static com.example.annuairelauriats.ui.home.HomeFragment.secteuuur;

public class SignalerFragment extends Fragment implements OnMapReadyCallback {
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private LatLng latLng_currennt;private GoogleMap gmap;
    private float zoom ;
    private Button top, bottom, right,left;
    private double lat, lon;
    private MapView mapView;private int differc;public static int statut;
    private List<Filiere> dates ;private List<Integer> organs;
    private SignalerViewModel signalerViewModel;
    private EditText nomEditText,prenomEditText,NumTeleEditText,usernameEditText ,passwordEditText 
    ,nouveau_org_nom ,date_debut_chez_org ,intitule_fonction_avec_org ,description_laureat ;
    private Spinner promotion ,filiere ,organisation ,organisation_secteur ;
    private ImageView imageView ,pick_date_debut_pop_up ;
    private TextView base64TextView ,org_select ,secteur_select,parameter_filiere,parametre_promotion;
    private RadioGroup radioOrgGroup ;private RadioButton org_deja,nvorg;private int organisation_selected;
    private Button registerButton ;private int filiere_selected;
    private ProgressBar loadingProgressBar ;RadioButton organis_selected_radio,organis_edited_radio;
    private ConstraintLayout constraintLayout ;
    private int year, month, day;
    private ScrollView scrollView;

    private String nom_laureat_static,prenom_laureat_static,numtel_laureat_static,promotion_laureat_static
            ,password_laureat_static,description_laureat_static;
    private int filiere_laureat_static,orgselected_laureat_static,checked_native;
    long checked_radio;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        signalerViewModel = ViewModelProviders.of(this).get(SignalerViewModel.class);
        View root = inflater.inflate(R.layout.signalerproblem, container, false);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar())
                .setTitle("Modifier Vos Informations");
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = root.findViewById(R.id.edit_infos_map_put_org);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        finds(root);
        others();
        return root;
    }
    private void show_popup() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.setContentView(R.layout.take_image);
        dialog.findViewById(R.id.pick_from_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenCamera();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.pick_from_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void OpenGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        this.startActivityForResult(gallery, 100);
    }
    private void OpenCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1337);
    }
    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //Intent cropIntent = new Intent("com.android.gallery.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", true);
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, 1);
        }
        catch (Exception anfe) {anfe.printStackTrace();
        prenomEditText.setText(anfe.toString());
        }
    }
    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            Uri imageUriii = data.getData();
            /*performCrop(imageUriii);*/
            try {
                InputStream imageStream;
                assert imageUriii != null;
                imageStream = Objects.requireNonNull(getContext()).getContentResolver().openInputStream(imageUriii);
                Bitmap selectedImageb = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(resize_bitmap(selectedImageb));
                base64TextView.setText("from gallery");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (resultCode == RESULT_OK && requestCode == 1337) {
            Bitmap selectedImage = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            imageView.setImageBitmap(resize_bitmap(selectedImage));
            base64TextView.setText("from camera");
        }
        else if (resultCode == RESULT_OK && requestCode == 1) {
            Uri image_cropped=data.getData();
            imageView.setImageURI(image_cropped);
        }
        else {
            Drawable drawable =getActivity().getDrawable(R.drawable.errorimage);
            assert drawable != null;
            imageView.setImageBitmap(resize_drawable(drawable));
            base64TextView.setText("");
        }
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
        gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                gmap.clear();
                MarkerOptions markerOptions = new MarkerOptions();
                lat = latLng.latitude;
                lon = latLng.longitude;
                markerOptions.position(latLng);
                gmap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                gmap.addMarker(markerOptions);
            }
        });
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
    private void finds(View root){

        nomEditText = root.findViewById(R.id.edit_infos_register_nom_laureat);
        prenomEditText = root.findViewById(R.id.edit_infos_register_prenom_laureat);
        NumTeleEditText = root.findViewById(R.id.edit_infos_register_telephone_laureat);
        usernameEditText = root.findViewById(R.id.edit_infos_usernamename);
        promotion = root.findViewById(R.id.edit_infos_snipper_promotion_laureat);
        filiere = root.findViewById(R.id.edit_infos_snipper_filiere_laureat);
        base64TextView = root.findViewById(R.id.edit_infos_register_image_base_64_laureat);
        imageView = root.findViewById(R.id.edit_infos_selected_file_image8laureat);
        passwordEditText = root.findViewById(R.id.edit_infos_passwordword);
        parameter_filiere = root.findViewById(R.id.edit_infos_filiere_text_view_register);
        parametre_promotion = root.findViewById(R.id.edit_infos_promotion_text_view_register);
        radioOrgGroup = root.findViewById(R.id.edit_infos_radio_organisation);
        organisation = root.findViewById(R.id.edit_infos_snipper_select_org);
        nouveau_org_nom = root.findViewById(R.id.edit_infos_snipper_ecrire_nom_org);
        organisation_secteur = root.findViewById(R.id.edit_infos_snipper_select_secteur_org);
        date_debut_chez_org = root.findViewById(R.id.edit_infos_date_pick_with_org);
        pick_date_debut_pop_up = root.findViewById(R.id.edit_infos_pick_date_button);
        intitule_fonction_avec_org = root.findViewById(R.id.edit_infos_intitule_fonction_with_org);
        description_laureat = root.findViewById(R.id.edit_infos_description_laureat);
        registerButton = root.findViewById(R.id.edit_infos_register);
        loadingProgressBar = root.findViewById(R.id.edit_infos_registering);
        constraintLayout = root.findViewById(R.id.edit_infos_constraint_layout);
        org_select = root.findViewById(R.id.edit_infos_org_text_view_register);
        secteur_select = root.findViewById(R.id.edit_infos_secteur_org_text_view_register);
        org_deja=root.findViewById(R.id.radio_organisation_existe);nvorg=root.findViewById(R.id.radio_organisation_non_existe);
        top=root.findViewById(R.id.edit_infos_go_top_1);bottom=root.findViewById(R.id.edit_infos_go_bottom_1);
        right=root.findViewById(R.id.edit_infos_go_right_1);left=root.findViewById(R.id.edit_infos_go_left_1);
        organis_selected_radio = root.findViewById(R.id.edit_infos_radio_organisation_existe);
        organis_edited_radio = root.findViewById(R.id.edit_infos_radio_organisation_non_existe);
        scrollView = root.findViewById(R.id.scrooool);


        List<String> secteurs = new ArrayList<>();
        secteurs.add("SELECTIONNER");secteurs.add("public");secteurs.add("prive");
        ArrayAdapter<String> list_org_adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,secteurs);
        list_org_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        organisation_secteur.setAdapter(list_org_adapter);

        dates = new ArrayList<>();organs = new ArrayList<>();
        try {
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
                    ArrayAdapter<String> list_adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,filieres);
                    list_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    filiere.setAdapter(list_adapter);
                }
            };
            connect_to_backend_array(getActivity(), Request.Method.GET,"/autres/filieres",
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
                            new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,organismes);
                    list_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    organisation.setAdapter(list_adapter);
                    remplir();
                }
            };
            connect_to_backend_array(getActivity(),Request.Method.GET,  "/autres/organismes",
                    null, listener_org, null);
        }
        catch (Exception ec){ec.printStackTrace();
        }
    }
    private void text_watcher(){

        signalerViewModel.getSignalerFormState().observe(this, new Observer<SignalerFormState>() {
            @Override
            public void onChanged(@Nullable SignalerFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                registerButton.setEnabled(registerFormState.isDataValid());
                if (registerFormState.isDataValid()) {
                    registerButton.setAlpha(1);
                }
                if (!registerFormState.isDataValid()) {
                    registerButton.setAlpha((float) 0.2);
                }
                if (registerFormState.getNomError() != null && statut!=4) {
                    nomEditText.setError(getString(registerFormState.getNomError()));
                }
                if (registerFormState.getPreNomError() != null && statut!=4) {
                    prenomEditText.setError(getString(registerFormState.getPreNomError()));
                }
                if (registerFormState.getFiliereError() != null && statut!=4) {
                    TextView errorText = (TextView) filiere.getSelectedView();
                    errorText.setError(getString(registerFormState.getFiliereError()));
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(getString(registerFormState.getFiliereError()));//changes the selected item text to this
                }
                if (registerFormState.getPromotionError() != null && statut!=4) {
                    TextView errorText = (TextView) promotion.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(getString(registerFormState.getPromotionError()));//changes the selected item text to this
                }
                if (registerFormState.getNumeroTelError() != null) {
                    NumTeleEditText.setError(getString(registerFormState.getNumeroTelError()));
                }
                if (registerFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(registerFormState.getUsernameError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(registerFormState.getPasswordError()));
                }
                if (registerFormState.getDateError() != null) {
                    date_debut_chez_org.setError(getString(registerFormState.getDateError()));
                }
                if (registerFormState.getImageError() != null) {
                    Drawable drawable =getActivity().getDrawable(R.drawable.errorimage);
                    assert drawable != null;
                    imageView.setImageBitmap(resize_drawable(drawable));
                }
                if (registerFormState.getIntituleError() != null) {
                    intitule_fonction_avec_org.setError(getString(registerFormState.getIntituleError()));
                }
                if (registerFormState.getDescError() != null) {
                    description_laureat.setError(getString(registerFormState.getDescError()));
                }
                if (registerFormState.getOrgError() != null) {
                    TextView errorText = (TextView) organisation.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(getString(registerFormState.getOrgError()));//changes the selected item text to this
                }
                if (registerFormState.getSecteurError() != null) {
                    TextView errorText = (TextView) organisation_secteur.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(getString(registerFormState.getSecteurError()));//changes the selected item text to this
                }
                if (registerFormState.getNv_org_nom_Error() != null) {
                    nouveau_org_nom.setError(getString(registerFormState.getNv_org_nom_Error()));
                }
            }
        });


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                loadingProgressBar.setVisibility(View.GONE);
                signalerViewModel.loginDataChanged(
                        nomEditText.getText().toString() + "",
                        prenomEditText.getText().toString() + "",
                        NumTeleEditText.getText().toString() + "",
                        usernameEditText.getText().toString() + "",
                        passwordEditText.getText().toString() + "",
                        base64TextView.getText().toString() + "",
                        promotion.getSelectedItem().toString() + "",
                        filiere.getSelectedItemId(),
                        intitule_fonction_avec_org.getText().toString() + "",
                        date_debut_chez_org.getText().toString() + "",
                        description_laureat.getText().toString() + "");
            }
        };
        if (statut!=4){
            nomEditText.addTextChangedListener(afterTextChangedListener);
            prenomEditText.addTextChangedListener(afterTextChangedListener);
        }

        NumTeleEditText.addTextChangedListener(afterTextChangedListener);
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        nouveau_org_nom.addTextChangedListener(afterTextChangedListener);
        base64TextView.addTextChangedListener(afterTextChangedListener);
        date_debut_chez_org.addTextChangedListener(afterTextChangedListener);
        intitule_fonction_avec_org.addTextChangedListener(afterTextChangedListener);
        description_laureat.addTextChangedListener(afterTextChangedListener);
    }
    private void others(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_popup();
            }
        });
        filiere.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {if (position==0){
                    promotion_peuplement(getActivity(), Calendar.getInstance().get(Calendar.YEAR)+2, promotion);
                }
                else {
                    filiere_selected=dates.get(position-1).get_Id();
                    promotion_peuplement(getActivity(), dates.get(position-1).getPrimier_promo(), promotion);
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        /**/organisation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    organisation_selected=organs.get(position-1);
                }
                else{organisation_selected=0;}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pick_date_debut_pop_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year_selected, int month_selected, int dayOfMonth) {
                        year = year_selected;
                        month = month_selected;
                        day = dayOfMonth;
                        date_debut_chez_org.setText(year + "-" + (month + 1) + "-" + day);
                    }
                }, year, month, day);
                dialog.show();
                dialog.updateDate(2019,11,12);
            }
        });
        radioOrgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = getView().findViewById(checkedId);
                if (radioButton.getText().toString().contains("Organisation")) {
                    checked_radio = 0;
                    nouveau_org_nom.setVisibility(View.GONE);
                    organisation_secteur.setVisibility(View.GONE);
                    constraintLayout.setVisibility(View.GONE);
                    organisation.setVisibility(View.VISIBLE);
                    org_select.setVisibility(View.VISIBLE);
                    secteur_select.setVisibility(View.GONE);
                } else if (radioButton.getText().toString().contains("pas reconnue")) {
                    nouveau_org_nom.setVisibility(View.VISIBLE);
                    checked_radio = 1;
                    organisation_secteur.setVisibility(View.VISIBLE);
                    constraintLayout.setVisibility(View.VISIBLE);
                    organisation.setVisibility(View.GONE);
                    org_select.setVisibility(View.GONE);
                    secteur_select.setVisibility(View.VISIBLE);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checked_radio==0 && !isSelectDropDownValid(organisation.getSelectedItemId())) {
                    Toast.makeText(getActivity(),"selectionner une organisation svp!"
                            ,Toast.LENGTH_LONG).show();
                }
                else if (checked_radio==1  && !isNomValid(nouveau_org_nom.getText().toString() )){
                    Toast.makeText(getActivity(),"veillez saisir le nom svp!"
                            ,Toast.LENGTH_LONG).show();
                }
                else if (checked_radio==1  && !isSelectDropDownValid(organisation_secteur.getSelectedItem().toString())){
                    Toast.makeText(getActivity(),"selectionner un secteur svp!"
                            ,Toast.LENGTH_LONG).show();}
                else if (checked_radio==1  && lat==0 && lon==0){
                    Toast.makeText(getActivity(),"positionner sur la carte svp!"
                            ,Toast.LENGTH_LONG).show();}
                else{

                    Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
                        @Override public void onResponse(JSONArray response) {
                            if (response.length()!=0){
                                try {
                                    if (response.getJSONObject(0).getString("email").equals(email_connected))
                                    {
                                        generate_sql();
                                    }
                                    else
                                    {
                                        usernameEditText.setError(getString(R.string.email_taken));
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    generate_sql();
                                }
                            }
                            else{
                                generate_sql();
                            }
                        }
                    };
                    connect_to_backend_array(getActivity(), Request.Method.GET
                            ,"/laureat/email/"+usernameEditText.getText().toString(),null
                            ,listener,null);
                    /*
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    Bitmap icon = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    registerButton.setShadowLayer(45,10,10,R.color.colorPrimary);
                    Toast.makeText(getActivity(),statut+"",Toast.LENGTH_LONG).show();
                signalerViewModel.update_data(
                        usernameEditText.getText().toString() + "",
                        passwordEditText.getText().toString() + "",
                        nomEditText.getText().toString() + "",
                        prenomEditText.getText().toString() + "",
                        NumTeleEditText.getText().toString() + "",
                        Classtest.encodeImage(icon) + "",
                         "",
                        promotion.getSelectedItem().toString() + "",
                        filiere.getSelectedItemId(),
                        organisation.getSelectedItemId(),
                        nouveau_org_nom.getText().toString() + "",
                        organisation_secteur.getSelectedItem().toString() + "",
                        intitule_fonction_avec_org.getText().toString() + "",
                        date_debut_chez_org.getText().toString() + "",
                        description_laureat.getText().toString() + "",checked_radio,lat,lon);*/
                }
            }
        });
    }
    private boolean isNomValid(String Nom) {
        return Nom != null && Nom.trim().length() > 3;
    }
    private boolean isSelectDropDownValid(long selcected_id){
        return selcected_id!=0;
    }
    private boolean isSelectDropDownValid(String selcected){
        return !selcected.equals("SELECTIONNER");
    }
    private int getpositionfromId(int id){
        int position=0;
        for (int i=0;i<dates.size();i++){
            if (dates.get(i).get_Id()==id){
                position=i;
            }
        }
        return position+1;
    }
    private int getpositionfromIdOrg(int id){
        int position=0;
        for (int i=0;i<organs.size();i++){
            if (organs.get(i)==id){
                position=i;
            }
        }
        return position+1;
    }
    private void remplir() {

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONArray response) {
                try
                {
                    JSONObject laureat = response.getJSONObject(0);
                    nomEditText.setText(laureat.getString("Nom"));
                    nom_laureat_static=laureat.getString("Nom");

                    prenomEditText.setText(laureat.getString("Prenom"));
                    prenom_laureat_static=laureat.getString("Prenom");

                    NumTeleEditText.setText(laureat.getString("Telephone"));
                    numtel_laureat_static=laureat.getString("Telephone");


                    filiere.setSelection(getpositionfromId(laureat.getInt("Filiere")));
                    filiere_laureat_static=laureat.getInt("Filiere");
                    int prm=dates.get(getpositionfromId(laureat.getInt("Filiere"))-1).getPrimier_promo()
                            ,promo= laureat.getInt("Promotion");
                    promotion_laureat_static=laureat.getInt("Promotion")+"";
                    promotion_peuplement(getActivity()
                            ,prm , promotion);
                    base64TextView.setText("initialised from web");
                    imageView.setImageBitmap(base64toImage(laureat.getString("photo")));
                    Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar())
                            .setTitle("Modifier vos informations");
                    usernameEditText.setText(laureat.getString("email"));
                    passwordEditText.setText(laureat.getString("Pass_word"));
                    password_laureat_static=laureat.getString("Pass_word");
                    description_laureat.setText(laureat.getString("Description"));
                    description_laureat_static=laureat.getString("Description");
                    statut=laureat.getInt("id_lesstatus");
                    if (statut==4){
                        nomEditText.setVisibility(View.GONE);prenomEditText.setVisibility(View.GONE);
                        filiere.setVisibility(View.GONE);promotion.setVisibility(View.GONE);
                        parameter_filiere.setVisibility(View.GONE);
                        parametre_promotion.setVisibility(View.GONE);
                    }
                    promotion.setSelection(promo-prm+1);
                    orgselected_laureat_static=laureat.getInt("org");
                    if (laureat.getInt("org")==0){
                        organis_edited_radio.setChecked(true);organis_selected_radio.setChecked(false);
                        nouveau_org_nom.setText(Orgedited);
                        if (secteuuur.equals("public")){organisation_secteur.setSelection(1);}
                        else if (secteuuur.equals("prive")){organisation_secteur.setSelection(2);}
                        else {organisation_secteur.setSelection(0);}

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(laatloong);checked_radio=1;checked_native=1;
                        gmap.addMarker(markerOptions);
                        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(laatloong, 12.0f));
                        lat=laatloong.latitude;lon=laatloong.longitude;
                    }
                    else{
                        organis_selected_radio.setChecked(true);organis_edited_radio.setChecked(false);
                        organisation.setSelection(getpositionfromIdOrg(laureat.getInt("org")));
                        checked_radio=0;checked_native=0;
                    }
                    date_debut_chez_org.setText(daaateee_deeebbuuuu);
                    intitule_fonction_avec_org.setText(intiiiitullee);
                    scrollView.setVisibility(View.VISIBLE);
                    text_watcher();
                    //dialog.dismiss();
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
        connect_to_backend_array(getContext(), Request.Method.GET,  "/laureat/id/"+email_connected,
                null, listener, errorListener);
    }
    private void generate_sql(){
        set0Pref(getActivity(),usernameEditText.getText().toString());
        int modifications_laureat=0;
        StringBuilder sql_laureat= new StringBuilder();
        sql_laureat.append("UPDATE laureats SET");
        if (statut!=4){
            if (!nom_laureat_static.equals(nomEditText.getText().toString())){
                modifications_laureat+=1;
                sql_laureat.append(" Nom= \"").append(nomEditText.getText().toString()).append( "\"  ");
            }
            if (!prenom_laureat_static.equals(prenomEditText.getText().toString())){
                modifications_laureat+=1;
                sql_laureat.append(",Prenom= \"").append(prenomEditText.getText().toString()).append( "\"  ");
            }
            if (filiere_selected!=filiere_laureat_static){
                sql_laureat.append(",Filiere= ").append(filiere_selected).append("  ");
                modifications_laureat+=1;
            }
            if (!promotion_laureat_static.equals(promotion.getSelectedItem().toString())){
                sql_laureat.append(",Promotion= ").append(promotion.getSelectedItem().toString()).append(" ");
                modifications_laureat+=1;
            }

            try {
                JSONObject statut_laureat= new JSONObject();
                statut_laureat.put("id_statut",3);
                statut_laureat.put("motif","re-inscrit");
                statut_laureat.put("id_laureat",usernameEditText.getText().toString());
                connect_to_backend(getActivity(),Request.Method.POST,
                        "/laureat/update_row_statut_laureat", statut_laureat
                        , new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                            }
                        },null);
            }
            catch (JSONException e) { e.printStackTrace(); }
        }
        if (!numtel_laureat_static.equals(NumTeleEditText.getText().toString())){
            sql_laureat.append(",Telephone= \"").append(NumTeleEditText.getText().toString()).append( "\"  ");
            modifications_laureat+=1;
        }
        if (!base64TextView.getText().toString().equals("initialised from web")){
            Bitmap icon = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            String image = encodeImage(icon);
            try {
                JSONObject jsonObject= new JSONObject();
                jsonObject.put("photo",image);jsonObject.put("email",usernameEditText.getText().toString());
                connect_to_backend(getActivity(), Request.Method.POST, "/laureat/update"
                        , jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getContext(),"image posted",Toast.LENGTH_LONG).show();
                            }
                        },null);
            }
            catch(Exception e){
                setclipboard(e.getMessage());
            }
        }
        if (!email_connected.equals(usernameEditText.getText().toString())){
            sql_laureat.append(",email= ").append( "\"").append(usernameEditText.getText().toString()).append( "\"  ");
            modifications_laureat+=1;
            connect_to_backend(getActivity(), Request.Method.GET, "/autres/requestAny/"
                            + "UPDATE laureat_org_association SET laureat=\""+usernameEditText.getText().toString()
                            +"\" WHERE laureat=\""+email_connected
                            +"\""
                    , null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    },null);
            connect_to_backend(getActivity(), Request.Method.GET, "/autres/requestAny/"
                            + "UPDATE organisme_en_attente SET laureat=\""+usernameEditText.getText().toString()
                            +"\" WHERE laureat=\""+email_connected
                            +"\""
                    , null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    },null);
            connect_to_backend(getActivity(), Request.Method.GET, "/autres/requestAny/"
                            + "UPDATE posts SET laureat=\""+usernameEditText.getText().toString()
                            +"\" WHERE laureat=\""+email_connected
                            +"\""
                    , null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    },null);
            connect_to_backend(getActivity(), Request.Method.GET, "/autres/requestAny/"
                            + "UPDATE laureat_statut SET id_laureat=\""+usernameEditText.getText().toString()
                            +"\" WHERE id_laureat=\""+email_connected
                            +"\""
                    , null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    },null);
        }
        if (!password_laureat_static.equals(passwordEditText.getText().toString())){
            sql_laureat.append(",Pass_word= ").append( "\"").append(passwordEditText.getText().toString()).append( "\"  ");
            modifications_laureat+=1;
        }
        if (!description_laureat_static.equals(description_laureat.getText().toString())){
            sql_laureat.append(",Description= ").append( "\"").append(description_laureat.getText().toString()).append( "\"  ");
            modifications_laureat+=1;
        }
        if (checked_radio==0){
            sql_laureat.append(",org= ").append(organisation_selected).append(" ");
            try {
                JSONObject org_laureat_new = new JSONObject();
                org_laureat_new.put("org",organisation_selected);
                org_laureat_new.put("laureat",usernameEditText.getText().toString());
                org_laureat_new.put("date_debut",date_debut_chez_org.getText().toString());
                org_laureat_new.put("fonction",intitule_fonction_avec_org.getText().toString());
                connect_to_backend(getActivity(),Request.Method.POST,"/laureat/new_row_org_laureat",org_laureat_new
                        ,new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                            }
                        }, null);
            }
            catch (Exception e){e.printStackTrace();}
        }
        if (checked_radio==1){
            sql_laureat.append(",org = 0 ");
            try {
                JSONObject new_org_attentente = new JSONObject();
                new_org_attentente.put("Nom",nouveau_org_nom.getText().toString());
                new_org_attentente.put("Latitude",lat);
                new_org_attentente.put("Longitude",lon);
                new_org_attentente.put("laureat",usernameEditText.getText().toString());
                new_org_attentente.put("intitule",intitule_fonction_avec_org.getText().toString());
                new_org_attentente.put("secteur",organisation_secteur.getSelectedItem().toString());
                new_org_attentente.put("date_debut",date_debut_chez_org.getText().toString());

                connect_to_backend(getActivity(),Request.Method.POST, "/autres/insert_org_attente", new_org_attentente
                        , new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                            }
                        },null);
            }
            catch (Exception e){e.printStackTrace();}
        }

        sql_laureat.append(" WHERE email= ").append( "\"").append(email_connected).append("\" ");
        sql_laureat.replace(19,20," ");

        if (modifications_laureat!=0){
            connect_to_backend(getActivity(), Request.Method.GET, "/autres/requestAny/" + sql_laureat
                    , null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    },null);
        }
        save_edits();
        email_connected = usernameEditText.getText().toString();

    }

    private void save_edits(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, new HelpFragment());
        fragmentTransaction.replace(R.id.nav_host_fragment, new HomeFragment());
        fragmentTransaction.commit();
    }
    private void setclipboard(String message){
        ClipboardManager cManager = (ClipboardManager) Objects.requireNonNull(getActivity()).getSystemService(
                Context.CLIPBOARD_SERVICE);
        ClipData cData = ClipData.newPlainText("text", message+"");
        if (cManager != null) {
            cManager.setPrimaryClip(cData);
            Toast.makeText(getActivity(),"copied to clipboard",Toast.LENGTH_LONG).show();
        }
    }
}
