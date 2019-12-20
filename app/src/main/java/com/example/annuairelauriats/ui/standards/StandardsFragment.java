package com.example.annuairelauriats.ui.standards;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.example.annuairelauriats.R;
import com.example.annuairelauriats.ui.home.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.example.annuairelauriats.ui.home.Classtest.base64toImage;
import static com.example.annuairelauriats.ui.home.Classtest.encodeImage;
import static com.example.annuairelauriats.ui.home.Classtest.is_file_exists;
import static com.example.annuairelauriats.ui.home.Classtest.load_raw;
import static com.example.annuairelauriats.ui.home.Classtest.resize_bitmap;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class StandardsFragment extends Fragment {
    private TextView result_http_client;private VideoView videoView;private EditText url;
    private ImageView imageView,imageViewm;private EditText password;
    private static Context context;
    private DatabaseHandler databaseHandler ;
    @Nullable
    public static Context getContexte() {
        return context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.standardscommunute, container, false);
        context=getActivity();
        url = root.findViewById(R.id.editText1);password=root.findViewById(R.id.pssssss);
        url.setText("israe");password.setText("hyouri sama");
        videoView = root.findViewById(R.id.videoView);
        result_http_client=root.findViewById(R.id.result_http_client);
        imageView = root.findViewById(R.id.ImageView01);imageViewm=root.findViewById(R.id.ImageView99);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()){videoView.pause(); }
                else{videoView.start();}
            }
        });
        imageView.setImageResource( R.drawable.ing);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        Button but_connect = root.findViewById(R.id.buttonSend);
        databaseHandler = new DatabaseHandler(getActivity());
        but_connect.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                try {
                    result_http_client.setText(is_file_exists(getContext(),url.getText().toString())+" file");
                    /*Bitmap icon = resize_bitmap(((BitmapDrawable) imageView.getDrawable()).getBitmap());
                    databaseHandler.insert_photo(encodeImage(icon) + "");
                    Bitmap base=base64toImage(databaseHandler.return_photo());
                    url.setText(base.getHeight()+" + "+base.getWidth());
                    imageViewm.setImageBitmap(base);
                    String lien=url.getText().toString(),mdp=password.getText().toString();
                    if (!(lien.isEmpty() && mdp.isEmpty())){
                        databaseHandler.insert_laureat(lien,mdp);
                    }
                    List<String> list = databaseHandler.return_laureats();
                    result_http_client.setText(""+list.size()+"\n"+"\n");
                    for (String e:list) {
                        result_http_client.append("\t"+e+"\n");
                    }
                    result_http_client.append("\n"+"\n");
                    Drawable drawable = Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ing);
                    assert drawable != null;
                    Bitmap b = ((BitmapDrawable)drawable).getBitmap();
                    int width = drawable.getIntrinsicWidth(),heigh=drawable.getIntrinsicHeight();
                    float scaleFactor =(float)200/Math.max(heigh,width);
                    int sizeX =(Integer) Math.round(width * scaleFactor);
                    int sizeY = (Integer) Math.round(heigh* scaleFactor);
                    result_http_client.append("drawable size : "+width+"  X "+heigh+"\n");
                    result_http_client.append("resize : "+sizeX+"  X "+sizeY+"\n");
                    result_http_client.append("scale factor : "+scaleFactor+"\n");
                    result_http_client.append("scale factor * 100: "+scaleFactor*100+"\n");
                    result_http_client.append("bitmap size : "+b.getWidth()+"  X "+b.getHeight()+"\n");
                    getPref();
                    result_http_client.setText(java.util.UUID.randomUUID().toString());
                    OpenGallery();
                    Bitmap icon = ((BitmapDrawable) imageView.getDrawable() ).getBitmap();
                    int height = icon.getHeight(),width = icon.getWidth();
                    result_http_client.setText(height+" * "+width);
                    ImageView img= getActivity().findViewById(R.id.ImageView99);
                    img.setImageBitmap(icon);
                    videoView.setVideoPath(url.getText().toString());
                    videoView.start();
                    OpenCamera();
                    show_popup();
                    String date = "2009-10-10";
                    int i = Integer.parseInt(date.substring(0,4).trim())+2000;
                    result_http_client.append("INT : "+i+"\n\n");
                    String string = "{\n" +
                            "\"organisme\":1,\"province\":djnfjj,\"filiere\":3,\"promotion\":\"2020\"\n" +
                            "}";
                    result_http_client.setText(Classtest.loadJSONfromCACHE(getContext()));
                    assert getFragmentManager() != null;
                    Fragment fragment = new HelpFragment();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    MainActivity.navigationView.setCheckedItem(R.id.nav_slideshow);

                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.nav_host_fragment, new SlideshowFragment());
                    //MainActivity.navigationView.getCheckedItem();
                    result_http_client.setText(MainActivity.navigationView.getCheckedItem().getGroupId()+""
                    + Classtest.getJsonObjectById(getActivity(),"myjson.json",1).toString()

                     );
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);
                    trans.commit();*/
                } catch (Exception e) {
                    e.printStackTrace();
                    result_http_client.append(e.toString());
                }
            }
        });


        return root;
    }


    public void OpenGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        this.startActivityForResult(gallery,100);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK && requestCode==100){
            final Uri imageUriii = data.getData();
            assert imageUriii != null;
            String uriString = imageUriii.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            String displayName = null;
            if (uriString.startsWith("content://")) {
                try (
                        Cursor cursor = getActivity().getContentResolver().query(imageUriii,
                                null, null,
                                null, null)) {
                    if (cursor != null && cursor.moveToFirst())
                    {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        result_http_client.setText(displayName);
                    }
                }
            }
            else if (uriString.startsWith("file://"))
            {
                displayName = myFile.getName();
                result_http_client.setText(displayName);
            }
            try {
                final InputStream imageStream;
                imageStream = getActivity().getContentResolver().openInputStream(imageUriii);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            imageView.setImageResource(R.drawable.errorimage);
            result_http_client.setText("please select img\n");
        }
    }
}
