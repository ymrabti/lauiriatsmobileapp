package com.example.annuairelauriats.ui.register;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
public class RegisterActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private RegisterViewModel registerViewModel;
    ImageView imageView;TextView base64TextView;private String Org_finale;
    private static final int PICK_IMAGE=100;private double lat,lon;
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
        final EditText nomEditText = findViewById(R.id.register_nom_laureat);
        final EditText prenomEditText = findViewById(R.id.register_prenom_laureat);
        final Spinner gender = findViewById(R.id.snipper_gender_laureat);
        final EditText NumTeleEditText = findViewById(R.id.register_telephone_laureat);
        final EditText usernameEditText = findViewById(R.id.usernamename);
        final Spinner promotion = findViewById(R.id.snipper_promotion_laureat);
        final Spinner filiere = findViewById(R.id.snipper_filiere_laureat);
        base64TextView = findViewById(R.id.register_image_base_64_laureat);
        imageView =findViewById(R.id.selected_file_image8laureat);
        final EditText passwordEditText = findViewById(R.id.passwordword);
        RadioGroup radioOrgGroup = findViewById(R.id.radio_organisation);
        final Spinner organisation = findViewById(R.id.snipper_select_org);
        final EditText nouveau_org_nom = findViewById(R.id.snipper_ecrire_nom_org);
        final Spinner organisation_secteur = findViewById(R.id.snipper_select_secteur_org);
        final Button registerButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.registering);
        final TextView go_to_login = findViewById(R.id.go_to_login);
        Classtest.spinner_list_adapt(getApplicationContext(),gender,"gender","genders.json",0);
        Classtest.spinner_list_adapt(getApplicationContext(),promotion,"promotion","promotions.json",0);
        Classtest.spinner_list_adapt(getApplicationContext(),filiere,"filiere","filieres.json",0);
        Classtest.spinner_list_adapt(getApplicationContext(),organisation,"org","organismes.json",0);
        Classtest.spinner_list_adapt(getApplicationContext(),organisation_secteur,"secteur","secteurs.json",0);
        //String org_secteur_selected = organisation_secteur.getSelectedItem().toString();

        imageView.setImageResource(R.drawable.avatar);
        imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OpenGallery();

                    }
                });
        go_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent i = new Intent(getApplicationContext(), LoginActivity.class);startActivity(i); }});

        radioOrgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton.getText().toString().contains("Organisation")){
                    nouveau_org_nom.setVisibility(View.GONE);
                    organisation_secteur.setVisibility(View.GONE);
                    mapView.setVisibility(View.GONE);
                    organisation.setVisibility(View.VISIBLE);
                }
                else if (radioButton.getText().toString().contains("pas reconnue")){
                    nouveau_org_nom.setVisibility(View.VISIBLE);
                    organisation_secteur.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.VISIBLE);
                    organisation.setVisibility(View.GONE);
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
                if (registerFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(registerFormState.getUsernameError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(registerFormState.getPasswordError()));
                }
                if (registerFormState.getNomError() != null) {
                    nomEditText.setError(getString(registerFormState.getNomError()));
                }
            }
        });

        registerViewModel.getRegistreResult().observe(this, new Observer<RegisterResult>() {
            @Override
            public void onChanged(@Nullable RegisterResult loginResult) {
                if (loginResult == null) { return; }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);//Complete and destroy login activity once successful
                finish(); }});

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) {
                if (nouveau_org_nom.getText().toString().isEmpty()){
                    Org_finale=organisation.getSelectedItem().toString();
                }
                else {
                    Org_finale = nouveau_org_nom.getText().toString();}
                registerViewModel.loginDataChanged(
                        usernameEditText.getText().toString()+"", passwordEditText.getText().toString()+"",
                        nomEditText.getText().toString()+"", prenomEditText.getText().toString()+"",
                        NumTeleEditText.getText().toString()+"",
                        base64TextView.getText().toString()+"", gender.getSelectedItem().toString()+"",
                        promotion.getSelectedItem().toString()+"",filiere.getSelectedItem().toString()+"",
                        Org_finale+""); }};
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        nomEditText.addTextChangedListener(afterTextChangedListener);
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
                            Org_finale+"");
                }
                return false;
            }
        });*/

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                if (nouveau_org_nom.getText().toString().isEmpty()){
                    Org_finale=organisation.getSelectedItem().toString();
                }
                else {
                    Org_finale = nouveau_org_nom.getText().toString();}
                registerViewModel.register(
                        usernameEditText.getText().toString()+"", passwordEditText.getText().toString()+"",
                        nomEditText.getText().toString()+"", prenomEditText.getText().toString()+"",
                        NumTeleEditText.getText().toString()+"",
                        base64TextView.getText().toString()+"", gender.getSelectedItem().toString()+"",
                        promotion.getSelectedItem().toString()+"",filiere.getSelectedItem().toString()+"",
                        Org_finale+"");
            }
        });
    }

    private void updateUiWithUser(RegisterUserView model) {
        // TODO : initiate successful logged in experience
        try {
            new_Laureat_Register(
                    model.getDisplayName()+"",
                    model.getPassWordUser()+"",
                    model.getLaureatNom()+"",
                    model.getLaureatPrenom()+"",
                    model.getLaureatNumTel()+"",
                    model.getLaureatImageBase64()+"",
                    model.getLaureatGender()+"",
                    model.getLaureatPromotion()+"",
                    model.getLaureatFiliere()+"",
                    model.getLaureatOrganisation()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent i = new Intent(getApplicationContext(), MainActivity.class);startActivity(i);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
        //Intent i = new Intent(getApplicationContext(), SettingsActivity.class);startActivity(i);
    }
    private void OpenGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }
    @Override protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK && requestCode==PICK_IMAGE){
            final Uri imageUriii = data.getData();
            try {
                final InputStream imageStream;
                assert imageUriii != null;
                imageStream = getContentResolver().openInputStream(imageUriii);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
                String base64 = encodeImage(selectedImage);
                base64TextView.setText(base64);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

//////////////////////////////////////   JSON FILES    /////////////////////////////////////////////////
    private void new_Laureat_Register(
            String email,
            String password,
            String nom,
            String prenom,
            String tel,
            String base64,
            String gender,
            String promotion,
            String filiere,
            String organisation) throws Exception {
        JSONObject new_Laureat = new JSONObject();
        int id_laureat_actuelle = Classtest.getLastID(this);
        new_Laureat.put("id",id_laureat_actuelle);
        new_Laureat.put("nom",nom);
        new_Laureat.put("prenom",prenom);
        new_Laureat.put("email",email);
        new_Laureat.put("password",password);
        new_Laureat.put("telephone",tel);
        new_Laureat.put("image",base64);
        new_Laureat.put("organisme", organisation);
        new_Laureat.put("latitude",lat);
        new_Laureat.put("longitude",lon);
        new_Laureat.put("genre",gender);
        new_Laureat.put("filiere",filiere);
        new_Laureat.put("promotion",promotion);
        Classtest.read_json_array(this,new_Laureat);
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
        gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.setTitle(""+marker.getPosition().toString());
                return false;
            }
        });
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}
