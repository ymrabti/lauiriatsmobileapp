package com.example.annuairelauriats.ui.signaler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.annuairelauriats.BuildConfig;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.home.Classtest;
import com.example.annuairelauriats.ui.register.RegisterActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.example.annuairelauriats.ui.home.Classtest.base64toImage;
import static com.example.annuairelauriats.ui.home.Classtest.filiers;
import static com.example.annuairelauriats.ui.home.Classtest.getJsonObjectBycle;
import static com.example.annuairelauriats.ui.home.Classtest.id_connected;
import static com.example.annuairelauriats.ui.home.Classtest.images_file;
import static com.example.annuairelauriats.ui.home.Classtest.laureats;
import static com.example.annuairelauriats.ui.home.Classtest.org_en_attente;
import static com.example.annuairelauriats.ui.home.Classtest.org_laureat;
import static com.example.annuairelauriats.ui.home.Classtest.organismes;
import static com.example.annuairelauriats.ui.home.Classtest.resize_bitmap;
import static com.example.annuairelauriats.ui.home.Classtest.resize_drawable;
import static com.example.annuairelauriats.ui.home.Classtest.spinner_list_adapt;

public class SignalerFragment extends Fragment implements OnMapReadyCallback {
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private LatLng latLng_currennt;
    private float zoom ;
    private Button top, bottom, right,left;
    private double lat, lon;
    private MapView mapView;

    private SignalerViewModel signalerViewModel;
    private EditText nomEditText,prenomEditText,NumTeleEditText,usernameEditText ,passwordEditText 
    ,nouveau_org_nom ,date_debut_chez_org ,intitule_fonction_avec_org ,description_laureat ;
    private Spinner gender ,promotion ,filiere ,organisation ,organisation_secteur ;
    private ImageView imageView ,pick_date_debut_pop_up ;
    private TextView base64TextView ,org_select ,secteur_select;
    private RadioGroup radioOrgGroup ;private RadioButton org_deja,nvorg;
    private Button registerButton ;
    private ProgressBar loadingProgressBar ;
    private ConstraintLayout constraintLayout ;
    private int year, month, day;
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
        nomEditText = root.findViewById(R.id.edit_infos_register_nom_laureat); 
        prenomEditText = root.findViewById(R.id.edit_infos_register_prenom_laureat); 
        gender = root.findViewById(R.id.edit_infos_snipper_gender_laureat);  
        NumTeleEditText = root.findViewById(R.id.edit_infos_register_telephone_laureat); 
        usernameEditText = root.findViewById(R.id.edit_infos_usernamename); 
        promotion = root.findViewById(R.id.edit_infos_snipper_promotion_laureat); 
        filiere = root.findViewById(R.id.edit_infos_snipper_filiere_laureat); 
        base64TextView = root.findViewById(R.id.edit_infos_register_image_base_64_laureat); 
        imageView = root.findViewById(R.id.edit_infos_selected_file_image8laureat);   
        passwordEditText = root.findViewById(R.id.edit_infos_passwordword); 
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

        spinner_list_adapt(getContext(), gender, "gender", "genders.json", 0);
        spinner_list_adapt(getContext(), filiere, "Nom", "filieres.json", 0);
        spinner_list_adapt(getContext(), organisation, "org", "organismes.json", 0);
        spinner_list_adapt(getContext(), organisation_secteur, "secteur", "secteurs.json", 0);
        Drawable drawable = getActivity().getDrawable(R.drawable.ing);
        assert drawable != null;
        //imageView.setImageBitmap(resize_drawable(drawable));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_popup();
            }
        });
        filiere.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    Classtest.promotion_peuplement(getActivity(), id, promotion);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
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
                checked_radio = checkedId;
                if (checkedId==2131296409) {
                    nouveau_org_nom.setVisibility(View.GONE);
                    organisation_secteur.setVisibility(View.GONE);
                    constraintLayout.setVisibility(View.GONE);
                    organisation.setVisibility(View.VISIBLE);
                    org_select.setVisibility(View.VISIBLE);
                    secteur_select.setVisibility(View.GONE);
                } else if (checkedId==2131296410) {
                    nouveau_org_nom.setVisibility(View.VISIBLE);
                    organisation_secteur.setVisibility(View.VISIBLE);
                    constraintLayout.setVisibility(View.VISIBLE);
                    organisation.setVisibility(View.GONE);
                    org_select.setVisibility(View.GONE);
                    secteur_select.setVisibility(View.VISIBLE);
                }
            }
        });

        try {
            JSONObject laurat_visitee = getJsonObjectBycle(getActivity(),"id",id_connected,laureats);
            JSONObject image= getJsonObjectBycle(getActivity(),"laureat",id_connected,images_file);
            JSONObject filier= getJsonObjectBycle(getActivity(),"id",laurat_visitee.getInt("filiere"),filiers);
            imageView.setImageBitmap(base64toImage(image.getString("image")));
            base64TextView.setText("from parent");
            nomEditText.setText(laurat_visitee.getString("nom"));
            prenomEditText.append(laurat_visitee.getString("prenom"));
            usernameEditText.setText(laurat_visitee.getString("email"));
            NumTeleEditText.setText(laurat_visitee.getString("telephone"));
            filiere.setSelection(3  /*filier.getString("Nom")*/);
            promotion.setSelection(5 /*laurat_visitee.getString("promotion")*/);
            JSONObject org_lau= getJsonObjectBycle(getActivity(),"id_laureat",id_connected,org_laureat);
            JSONObject org_lau_attente= getJsonObjectBycle(getActivity(),"laureat",id_connected,org_en_attente);
            if (!org_lau.isNull("id_org")){
                JSONObject org= getJsonObjectBycle(getActivity(),"id",org_lau.getInt("id_org"),organismes);
                organisation.setSelection(2/*org.getString("org")*/);
                lat = org.getDouble("latitude");
                lon =org.getDouble("longitude");
                org_deja.setChecked(true);nvorg.setChecked(false);
            }
            else{
                nouveau_org_nom.setText(org_lau_attente.getString("org"));
                lat =org_lau_attente.getDouble("latitude");lon=org_lau_attente.getDouble("longitude");
                org_deja.setChecked(false);nvorg.setChecked(true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


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
                if (registerFormState.getNomError() != null) {
                    nomEditText.setError(getString(registerFormState.getNomError()));
                }
                if (registerFormState.getPreNomError() != null) {
                    prenomEditText.setError(getString(registerFormState.getPreNomError()));
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
                if (registerFormState.getGenderError() != null) {
                    TextView errorText = (TextView) gender.getSelectedView();
                    errorText.setError(getString(registerFormState.getGenderError()));
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(getString(registerFormState.getGenderError()));//changes the selected item text to this
                }
                if (registerFormState.getFiliereError() != null) {
                    TextView errorText = (TextView) filiere.getSelectedView();
                    errorText.setError(getString(registerFormState.getFiliereError()));
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(getString(registerFormState.getFiliereError()));//changes the selected item text to this
                }
                if (registerFormState.getPromotionError() != null) {
                    TextView errorText = (TextView) promotion.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(getString(registerFormState.getPromotionError()));//changes the selected item text to this
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
                        gender.getSelectedItem().toString() + "",
                        promotion.getSelectedItem().toString() + "",
                        filiere.getSelectedItemId(),
                        organisation.getSelectedItemId(),
                        nouveau_org_nom.getText().toString() + "",
                        organisation_secteur.getSelectedItem().toString() + "",
                        intitule_fonction_avec_org.getText().toString() + "",
                        date_debut_chez_org.getText().toString() + "",
                        description_laureat.getText().toString() + "",
                        checked_radio);
            }
        };
        nomEditText.addTextChangedListener(afterTextChangedListener);
        prenomEditText.addTextChangedListener(afterTextChangedListener);
        NumTeleEditText.addTextChangedListener(afterTextChangedListener);
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        nouveau_org_nom.addTextChangedListener(afterTextChangedListener);
        base64TextView.addTextChangedListener(afterTextChangedListener);
        date_debut_chez_org.addTextChangedListener(afterTextChangedListener);
        intitule_fonction_avec_org.addTextChangedListener(afterTextChangedListener);
        description_laureat.addTextChangedListener(afterTextChangedListener);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                Bitmap icon = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                registerButton.setShadowLayer(45,10,10,R.color.colorPrimary);
                signalerViewModel.update_data(
                        usernameEditText.getText().toString() + "",
                        passwordEditText.getText().toString() + "",
                        nomEditText.getText().toString() + "",
                        prenomEditText.getText().toString() + "",
                        NumTeleEditText.getText().toString() + "",
                        Classtest.encodeImage(icon) + "",
                        gender.getSelectedItem().toString() + "",
                        promotion.getSelectedItem().toString() + "",
                        filiere.getSelectedItemId(),
                        organisation.getSelectedItemId(),
                        nouveau_org_nom.getText().toString() + "",
                        organisation_secteur.getSelectedItem().toString() + "",
                        intitule_fonction_avec_org.getText().toString() + "",
                        date_debut_chez_org.getText().toString() + "",
                        description_laureat.getText().toString() + "",checked_radio,lat,lon);
            }
        });
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
    public void OpenGallery() {
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
        final GoogleMap gmap;
        gmap = googleMap;
        gmap.setMinZoomPreference(1);
        gmap.setIndoorEnabled(true);
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat,lon));
        gmap.addMarker(markerOptions);
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lon), 12.0f));
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
}
