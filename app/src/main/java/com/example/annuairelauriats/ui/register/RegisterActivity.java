package com.example.annuairelauriats.ui.register;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.view.View;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.home.Classtest;
import com.example.annuairelauriats.ui.login.LoginActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import static com.example.annuairelauriats.ui.home.Classtest.resize_bitmap;
import static com.example.annuairelauriats.ui.home.Classtest.resize_drawable;


public class RegisterActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static double lat, lon;
    private static Context context;
    ImageView imageView;
    TextView base64TextView;
    long checked_radio;
    private MapView mapView;
    private int year, month, day;
    private RegisterViewModel registerViewModel;
    private float zoom ;
    private LatLng latLng_currennt;
    private Button top, bottom, right,left;
    public static Context getContextext() {
        return context;
    }
    @SuppressLint("SetTextI18n")
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.map_put_org);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        final EditText nomEditText = findViewById(R.id.register_nom_laureat);  // Laureat                               1       1
        final EditText prenomEditText = findViewById(R.id.register_prenom_laureat);  // Laureat                         2       2
        final Spinner gender = findViewById(R.id.snipper_gender_laureat);  // Laureat                                   3       3
        final EditText NumTeleEditText = findViewById(R.id.register_telephone_laureat);  // Laureat                     4       4
        final EditText usernameEditText = findViewById(R.id.usernamename);  // Laureat                                  5       5
        final Spinner promotion = findViewById(R.id.snipper_promotion_laureat);  // Laureat                             6       6
        final Spinner filiere = findViewById(R.id.snipper_filiere_laureat);  // filier get position put to  Laureat     7       7
        base64TextView = findViewById(R.id.register_image_base_64_laureat);  // system                                          8
        imageView = findViewById(R.id.selected_file_image8laureat);   // system
        final EditText passwordEditText = findViewById(R.id.passwordword);  // Laureat                                  8       9
        final RadioGroup radioOrgGroup = findViewById(R.id.radio_organisation);  // system
        final Spinner organisation = findViewById(R.id.snipper_select_org);  // org get id put to Laureat_Org                  10
        final EditText nouveau_org_nom = findViewById(R.id.snipper_ecrire_nom_org);  // system                                  11
        final Spinner organisation_secteur = findViewById(R.id.snipper_select_secteur_org);  // system                          12
        final EditText date_debut_chez_org = findViewById(R.id.date_pick_with_org);  // Laureat_Org                             13
        final ImageView pick_date_debut_pop_up = findViewById(R.id.pick_date_button);  // system
        final EditText intitule_fonction_avec_org = findViewById(R.id.intitule_fonction_with_org);  // Laureat_Org              14
        final EditText description_laureat = findViewById(R.id.description_laureat);  // Laureat_Org                    9       15
        final Button registerButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.registering);
        final TextView go_to_login = findViewById(R.id.go_to_login);
        final ConstraintLayout constraintLayout = findViewById(R.id.constraint_layout);

        top=findViewById(R.id.go_top_1);bottom=findViewById(R.id.go_bottom_1);
        right=findViewById(R.id.go_right_1);left=findViewById(R.id.go_left_1);
        Classtest.spinner_list_adapt(getApplicationContext(), gender, "gender", "genders.json", 0);
        Classtest.spinner_list_adapt(getApplicationContext(), filiere, "Nom", "filieres.json", 0);
        Classtest.spinner_list_adapt(getApplicationContext(), organisation, "org", "organismes.json", 0);
        Classtest.spinner_list_adapt(getApplicationContext(), organisation_secteur, "secteur", "secteurs.json", 0);
        final TextView org_select = findViewById(R.id.org_text_view_register);
        final TextView secteur_select = findViewById(R.id.secteur_org_text_view_register);
        Drawable drawable = getDrawable(R.drawable.ing);
        assert drawable != null;
        imageView.setImageBitmap(resize_drawable(drawable));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_popup();
            }
        });
        go_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });
        context = this;
        filiere.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    Classtest.promotion_peuplement(RegisterActivity.this, id, promotion);
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
                Dialog dialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year_selected, int month_selected, int dayOfMonth) {
                        year = year_selected;
                        month = month_selected;
                        day = dayOfMonth;
                        date_debut_chez_org.setText(year + "-" + (month + 1) + "-" + day);
                    }
                }, year, month, day);
                dialog.show();
            }
        });
        radioOrgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                checked_radio = checkedId;
                if (radioButton.getText().toString().contains("Organisation")) {
                    nouveau_org_nom.setVisibility(View.GONE);
                    organisation_secteur.setVisibility(View.GONE);
                    constraintLayout.setVisibility(View.GONE);
                    organisation.setVisibility(View.VISIBLE);
                    org_select.setVisibility(View.VISIBLE);
                    secteur_select.setVisibility(View.GONE);
                } else if (radioButton.getText().toString().contains("pas reconnue")) {
                    nouveau_org_nom.setVisibility(View.VISIBLE);
                    organisation_secteur.setVisibility(View.VISIBLE);
                    constraintLayout.setVisibility(View.VISIBLE);
                    organisation.setVisibility(View.GONE);
                    org_select.setVisibility(View.GONE);
                    secteur_select.setVisibility(View.VISIBLE);
                }
            }
        });
        registerViewModel.getREgisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                registerButton.setEnabled(registerFormState.isDataValid());
                if (registerFormState.isDataValid()) {
                    registerButton.setBackgroundResource(R.drawable.img_356567_unclicked);
                }
                if (!registerFormState.isDataValid()) {
                    registerButton.setBackgroundResource(R.drawable.register_disabled);
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
                    Drawable drawable = getDrawable(R.drawable.errorimage);
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
                registerViewModel.loginDataChanged(
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
                registerButton.setBackgroundResource(R.drawable.register);
                Bitmap icon = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                registerViewModel.register(
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
                        description_laureat.getText().toString() + "");
            }
        });

    }
    //////////////////////////////////  ACTION IMAGE   /////////////////////////////////////////
    public void OpenGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        this.startActivityForResult(gallery, 100);
    }
    private void OpenCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1337);
    }
    private void show_popup() {
        final Dialog dialog = new Dialog(this);
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
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            final Uri imageUriii = data.getData();
            try {
                final InputStream imageStream;
                assert imageUriii != null;
                imageStream = getContentResolver().openInputStream(imageUriii);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(resize_bitmap(selectedImage));
                base64TextView.setText("selected from gallery\n");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && requestCode == 1337) {
            Bitmap selectedImage = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            assert selectedImage != null;
            imageView.setImageBitmap(resize_bitmap(selectedImage));
            base64TextView.setText("selected from camera\n");
        } else {
            Drawable drawable = getDrawable(R.drawable.errorimage);
            assert drawable != null;
            imageView.setImageBitmap(resize_drawable(drawable));
            base64TextView.setText("");
        }
    }
    //////////////////////////////////  MAP   /////////////////////////////////////////
    @Override public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override protected void onResume() { super.onResume();mapView.onResume(); }
    @Override protected void onStart() { super.onStart();mapView.onStart(); }
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