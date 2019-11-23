package com.example.annuairelauriats.ui.register;

import android.app.Activity;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;
import com.example.annuairelauriats.SettingsActivity;
import com.example.annuairelauriats.ui.login.LoginActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private RegisterViewModel registerViewModel;
    ImageView imageView;
    Button button_select_photo_laureat ;
    private static final int PICK_IMAGE=100;
    private RadioButton radioButton;
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
        final EditText usernameEditText = findViewById(R.id.usernamename);
        final EditText passwordEditText = findViewById(R.id.passwordword);
        final Button loginButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.registering);
        final TextView go_to_login = findViewById(R.id.go_to_login);
        go_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        registerViewModel.getREgisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        registerViewModel.getRegistreResult().observe(this, new Observer<RegisterResult>() {
            @Override
            public void onChanged(@Nullable RegisterResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.loginDataChanged(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerViewModel.register(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                registerViewModel.register(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
        RadioGroup radioOrgGroup = findViewById(R.id.radio_organisation);
    radioOrgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            radioButton = findViewById(checkedId);
            LinearLayout linearLayout;LinearLayout linearLayoutChecked;
            if (radioButton.getText().toString().contains("Organisation")){
                linearLayout  = findViewById(R.id.org_no_connue);linearLayout.setVisibility(View.GONE);
                linearLayoutChecked  = findViewById(R.id.org_connue);linearLayout.setVisibility(View.VISIBLE);
            }
            else{
                linearLayout  = findViewById(R.id.org_connue);linearLayout.setVisibility(View.GONE);
                linearLayoutChecked  = findViewById(R.id.org_no_connue);linearLayout.setVisibility(View.VISIBLE);
            }
        }
    });


        Spinner gender = findViewById(R.id.snipper_gender_laureat);
        Spinner promotion = findViewById(R.id.snipper_promotion_laureat);
        Spinner filiere = findViewById(R.id.snipper_filiere_laureat);
        List genders = new ArrayList();genders.add("Male");genders.add("Female");

        List promotions = peupler_list("promotion","promotions.json");

        List filieress = peupler_list("filiere","filieres.json");

        ArrayAdapter genderadapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,genders);
        genderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter promotionsadapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,promotions);
        promotionsadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter filiereadapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,filieress);
        filiereadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        gender.setAdapter(genderadapter);promotion.setAdapter(promotionsadapter);filiere.setAdapter(filiereadapter);

        button_select_photo_laureat = findViewById(R.id.select_file_image8laureat);
        button_select_photo_laureat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OpenGallery();

                    }
                });
        imageView =findViewById(R.id.selected_file_image8laureat);

        //write_file_data(laureatun,"younes.txt");write_file_data(laureattrois,"sara.txt");
        //write_file_data(laureatdeux,"dodji.txt");write_file_data(laureatquatre,"ol.txt");
    }

    private void updateUiWithUser(RegisterUserView model) {
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(),model.getDisplayName() , Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);startActivity(i);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), SettingsActivity.class);startActivity(i);
    }
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
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
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

    private void OpenGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK && requestCode==PICK_IMAGE){
            final Uri imageUriii = data.getData();
            try {
                final InputStream imageStream;
                assert imageUriii != null;
                imageStream = getContentResolver().openInputStream(imageUriii);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> peupler_list(String Champ,String file){
        List<String> list_a_peupler = new ArrayList<>();
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
            File myExternalFile = new File(getExternalFilesDir("Annuaire"), fichier);
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

    @Override public void onSaveInstanceState(Bundle outState) {
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
}
