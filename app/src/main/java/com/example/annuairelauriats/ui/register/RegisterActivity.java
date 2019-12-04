package com.example.annuairelauriats.ui.register;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;
import com.example.annuairelauriats.MainActivity;
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
import java.util.Calendar;
import java.util.Objects;

import android.app.Dialog;

import static com.example.annuairelauriats.ui.home.Classtest.id_connected;

public class RegisterActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private RegisterViewModel registerViewModel;
    ImageView imageView;private int year,month,day;
    TextView base64TextView;
    private double lat, lon;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerViewModel = ViewModelProviders.of(this, new RegisterViewModelFactory()).get(RegisterViewModel.class);
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
        RadioGroup radioOrgGroup = findViewById(R.id.radio_organisation);  // system
        final Spinner organisation =  findViewById(R.id.snipper_select_org);  // org get id put to Laureat_Org                  10
        final EditText nouveau_org_nom = findViewById(R.id.snipper_ecrire_nom_org);  // system                                  11
        final Spinner organisation_secteur = findViewById(R.id.snipper_select_secteur_org);  // system                          12
        final EditText date_debut_chez_org = findViewById(R.id.date_pick_with_org);  // Laureat_Org                             13
        final ImageView pick_date_debut_pop_up = findViewById(R.id.pick_date_button);  // system
        final EditText intitule_fonction_avec_org = findViewById(R.id.intitule_fonction_with_org);  // Laureat_Org              14
        final EditText description_laureat = findViewById(R.id.description_laureat);  // Laureat_Org                    9       15
        final Button registerButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.registering);
        final TextView go_to_login = findViewById(R.id.go_to_login);
        Classtest.spinner_list_adapt(getApplicationContext(), gender, "gender", "genders.json", 0);
        Classtest.spinner_list_adapt(getApplicationContext(), filiere, "Nom", "filieres.json", 0);
        Classtest.spinner_list_adapt(getApplicationContext(), organisation, "org", "organismes.json", 0);
        Classtest.spinner_list_adapt(getApplicationContext(), organisation_secteur, "secteur", "secteurs.json", 0);

        final TextView org_select = findViewById(R.id.org_text_view_register);
        final TextView secteur_select = findViewById(R.id.secteur_org_text_view_register);



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


        filiere.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try { Classtest.promotion_peuplement(RegisterActivity.this,id,promotion); }
                catch (Exception e) { e.printStackTrace(); } }
                @Override public void onNothingSelected(AdapterView<?> parentView) { }});

        pick_date_debut_pop_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year_selected, int month_selected, int dayOfMonth) {
                        year = year_selected;
                        month = month_selected;
                        day = dayOfMonth;
                        date_debut_chez_org.setText(year+"-"+(month+1)+"-"+day);
                    }
                },year,month,day);
                dialog.show(); }});

        radioOrgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton.getText().toString().contains("Organisation"))
                {
                    nouveau_org_nom.setVisibility(View.GONE);
                    organisation_secteur.setVisibility(View.GONE);
                    mapView.setVisibility(View.GONE);
                    organisation.setVisibility(View.VISIBLE);
                    org_select.setVisibility(View.VISIBLE);
                    secteur_select.setVisibility(View.GONE);
                }
                else if (radioButton.getText().toString().contains("pas reconnue"))
                {
                    nouveau_org_nom.setVisibility(View.VISIBLE);
                    organisation_secteur.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.VISIBLE);
                    organisation.setVisibility(View.GONE);
                    org_select.setVisibility(View.GONE);
                    secteur_select.setVisibility(View.VISIBLE);
                }
                date_debut_chez_org.setVisibility(View.VISIBLE);
                intitule_fonction_avec_org.setVisibility(View.VISIBLE);
                pick_date_debut_pop_up.setVisibility(View.VISIBLE);
            }
        });



        registerViewModel.getREgisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                registerButton.setEnabled(registerFormState.isDataValid());
                if (registerFormState.isDataValid()){registerButton.setBackgroundResource(R.drawable.register);}
                if (!registerFormState.isDataValid()){registerButton.setBackgroundResource(R.drawable.register_disabled);}
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
                    TextView errorText = (TextView)gender.getSelectedView();
                    errorText.setError(getString(registerFormState.getGenderError()));
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(getString(registerFormState.getGenderError()));//changes the selected item text to this
                }
                if (registerFormState.getFiliereError() != null) {
                    TextView errorText = (TextView)filiere.getSelectedView();
                    errorText.setError(getString(registerFormState.getFiliereError()));
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(getString(registerFormState.getFiliereError()));//changes the selected item text to this
                }
                if (registerFormState.getPromotionError() != null) {
                    TextView errorText = (TextView)promotion.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(getString(registerFormState.getPromotionError()));//changes the selected item text to this
                }
                if (registerFormState.getImageError() != null) {
                    imageView.setImageResource(R.drawable.errorimage);
                }
            }
        });

        registerViewModel.getRegistreResult().observe(this, new Observer<RegisterResult>() {
            @Override
            public void onChanged(@Nullable RegisterResult registerResult) {
                if (registerResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (registerResult.getError() != null) {
                    showLoginFailed(registerResult.getError());
                }
                if (registerResult.getSuccess() != null) {
                    updateUiWithUser(registerResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);//Complete and destroy login activity once successful
                finish();
            }
        });
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) {
                registerViewModel.loginDataChanged(
                        nomEditText.getText().toString() +"",
                        prenomEditText.getText().toString()+"" ,
                        NumTeleEditText.getText().toString()+"" ,
                        usernameEditText.getText().toString()+"" ,
                        passwordEditText.getText().toString()+"" ,
                        base64TextView.getText().toString() +"",
                        gender.getSelectedItem().toString()+"" ,
                        promotion.getSelectedItem().toString()+"",
                        filiere.getSelectedItemId(),
                        organisation.getSelectedItemId() ,
                        nouveau_org_nom.getText().toString()+"",
                        organisation_secteur.getSelectedItem().toString()+"",
                        intitule_fonction_avec_org.getText().toString()+"",
                        date_debut_chez_org.getText().toString()+"",
                        description_laureat.getText().toString()+"");
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
        /*passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerViewModel.register(
                            usernameEditText.getText().toString()+"", passwordEditText.getText().toString()+"",
                            nomEditText.getText().toString()+"", prenomEditText.getText().toString()+"",
                            NumTeleEditText.getText().toString()+"",
                            base64TextView.getText().toString()+"", gender.getSelectedItem().toString()+"",
                            promotion.getSelectedItem().toString()+"",filiere.getSelectedItem().toString()+"",
                            "");
                }
                return false;
            }
        });*/

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                Bitmap icon = ((BitmapDrawable) imageView.getDrawable() ).getBitmap();
                registerViewModel.register(
                        usernameEditText.getText().toString()+"" ,
                        passwordEditText.getText().toString()+"" ,
                        nomEditText.getText().toString() +"",
                        prenomEditText.getText().toString()+"" ,
                        NumTeleEditText.getText().toString()+"" ,
                        Classtest.encodeImage(icon) +"",
                        gender.getSelectedItem().toString()+"" ,
                        promotion.getSelectedItem().toString()+"",
                        filiere.getSelectedItemId() ,
                        organisation.getSelectedItemId() ,
                        nouveau_org_nom.getText().toString()+"",
                        organisation_secteur.getSelectedItem().toString()+"",
                        intitule_fonction_avec_org.getText().toString()+"",
                        date_debut_chez_org.getText().toString()+"",
                        description_laureat.getText().toString()+"");
            }
        });

        findViewById(R.id.enregitrer_vous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(main);
            }
        });
    }
    private void updateUiWithUser(RegisterUserView model) {
        try {
            Calendar rightNow = Calendar.getInstance();
            int seconde = rightNow.get(Calendar.SECOND);int minute = rightNow.get(Calendar.MINUTE);int heur = rightNow.get(Calendar.HOUR_OF_DAY);
            int jour = rightNow.get(Calendar.DAY_OF_MONTH);int mois = rightNow.get(Calendar.MONTH)+1;int annee = rightNow.get(Calendar.YEAR);
            String dateNow = annee+"-"+mois+"-"+jour+" "+heur+":"+minute+":"+seconde;
            int id_laureat_actuelle = Classtest.getLastID(this,Classtest.laureats);
            id_connected = id_laureat_actuelle;
            Classtest.new_Laureat_Register(
                    this,id_laureat_actuelle,
                    model.getLaureatNom()+"", model.getLaureatPrenom() +"", model.getLaureatGender()+"",
                    model.getLaureatPromotion()+"", model.getLaureatFiliere(), model.getEmailUser()+"",
                    model.getPassWordUser()+"", model.getLaureatNumTel()+"", dateNow+"",
                    model.getDescription()+"");
            Classtest.setNewImgLaureat(this,model.getLaureatImageBase64(),id_laureat_actuelle);
            if(!model.getNomOrgEdited().isEmpty()){
                Classtest.new_org_attente_admin(this,model.getNomOrgEdited(),id_laureat_actuelle,lat,lon,model.getSecteurOrgEdited(),
                        model.getDate_debut_travail_chez_org()+"",model.getInitulePost()+"");
            }
            else{
                Classtest.new_org_laureat(this,model.getOrg_selected(),id_laureat_actuelle,
                        model.getDate_debut_travail_chez_org(),model.getInitulePost());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent i = new Intent(getApplicationContext(), MainActivity.class);startActivity(i);
    }
    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
        //Intent i = new Intent(getApplicationContext(), SettingsActivity.class);startActivity(i);
    }
    //////////////////////////////////  ACTION IMAGE   /////////////////////////////////////////

    public void OpenGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        this.startActivityForResult(gallery,100);
    }
    private void OpenCamera(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1337);
    }
    private void show_popup(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.take_image);
        dialog.findViewById(R.id.pick_from_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenCamera();dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.pick_from_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK && requestCode==100){
            final Uri imageUriii = data.getData();
            try {
                final InputStream imageStream;
                assert imageUriii != null;
                imageStream = getContentResolver().openInputStream(imageUriii);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
                //String base64 = Classtest.encodeImage(selectedImage);
                base64TextView.setText("selected from gallery\n");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if(resultCode==RESULT_OK && requestCode==1337){
            Bitmap image = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            imageView.setImageBitmap(image);
            //String base64 = Classtest.encodeImage(image);
            base64TextView.setText("selected from camera\n");
        }
        else {
            imageView.setImageResource(R.drawable.errorimage);
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
    @Override protected void onStop() { super.onStop();mapView.onStop(); }
    @Override protected void onPause() { mapView.onPause();super.onPause(); }
    @Override protected void onDestroy() { mapView.onDestroy();super.onDestroy(); }
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
                lat = latLng.latitude;lon = latLng.longitude;
                markerOptions.position(latLng);
                gmap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                gmap.addMarker(markerOptions);
            }
        });
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}