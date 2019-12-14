package com.example.annuairelauriats.ui.signaler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.example.annuairelauriats.BuildConfig;
import com.example.annuairelauriats.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.example.annuairelauriats.ui.home.Classtest.resize_bitmap;
import static com.example.annuairelauriats.ui.home.Classtest.resize_drawable;
import static com.example.annuairelauriats.ui.home.Classtest.spinner_list_adapt;

public class SignalerFragment extends Fragment {

    private SignalerViewModel signalerViewModel;
    private EditText nomEditText,prenomEditText,NumTeleEditText,usernameEditText ,passwordEditText 
    ,nouveau_org_nom ,date_debut_chez_org ,intitule_fonction_avec_org ,description_laureat ;
    private Spinner gender ,promotion ,filiere ,organisation ,organisation_secteur ;
    private ImageView imageView ,pick_date_debut_pop_up ;
    private TextView base64TextView ,org_select ,secteur_select;
    private RadioGroup radioOrgGroup ;
    private Button registerButton ;
    private ProgressBar loadingProgressBar ;
    private ConstraintLayout constraintLayout ;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        signalerViewModel = ViewModelProviders.of(this).get(SignalerViewModel.class);
        View root = inflater.inflate(R.layout.signalerproblem, container, false);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar())
                .setTitle("Modifier Vos Informations");
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
        spinner_list_adapt(getContext(), gender, "gender", "genders.json", 0);
        spinner_list_adapt(getContext(), filiere, "Nom", "filieres.json", 0);
        spinner_list_adapt(getContext(), organisation, "org", "organismes.json", 0);
        spinner_list_adapt(getContext(), organisation_secteur, "secteur", "secteurs.json", 0);
        Drawable drawable = getActivity().getDrawable(R.drawable.ing);
        assert drawable != null;
        imageView.setImageBitmap(resize_drawable(drawable));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_popup();
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
            performCrop(imageUriii);
            /*try {
                InputStream imageStream;
                assert imageUriii != null;
                imageStream = Objects.requireNonNull(getContext()).getContentResolver().openInputStream(imageUriii);
                Bitmap selectedImageb = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(resize_bitmap(selectedImageb));
            } catch (Exception e) {
                e.printStackTrace();
                nomEditText.setText(e.toString());
            }*/
        }
        else if (resultCode == RESULT_OK && requestCode == 1337) {
            Bitmap selectedImage = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            imageView.setImageBitmap(resize_bitmap(selectedImage));
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
}
