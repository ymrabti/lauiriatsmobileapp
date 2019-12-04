package com.example.annuairelauriats.ui.standards;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.example.annuairelauriats.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import static com.example.annuairelauriats.ui.home.Classtest.is_login_valid;

public class StandardsFragment extends Fragment {

    private TextView result_http_client;private VideoView videoView;private EditText url;
    private ImageView imageView;private EditText password;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.standardscommunute, container, false);
        url = root.findViewById(R.id.editText1);password=root.findViewById(R.id.pssssss);
        videoView = root.findViewById(R.id.videoView);
        result_http_client=root.findViewById(R.id.result_http_client);
        imageView = root.findViewById(R.id.ImageView01);
        //url.setText("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
        //url.setText("https://docs.google.com/file/d/1QQUAGvruSoUGfcUO4_SjUc2VuQq34n4i/preview");
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()){videoView.pause(); }
                else{videoView.start();}
            }
        });
        imageView.setImageResource( R.drawable.ing);
        Button but_connect = root.findViewById(R.id.buttonSend);
        but_connect.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                try {
                    result_http_client.setText("exists : "+is_login_valid(getContext(),
                            url.getText().toString()+"",
                            password.getText().toString()+""));
                    /*OpenGallery();
                    Bitmap icon = ((BitmapDrawable) imageView.getDrawable() ).getBitmap();
                    int height = icon.getHeight(),width = icon.getWidth();
                    result_http_client.setText(height+" * "+width);
                    ImageView img= getActivity().findViewById(R.id.ImageView99);
                    img.setImageBitmap(icon);
                    videoView.setVideoPath(url.getText().toString());
                    videoView.start();*/
                    /*OpenCamera();
                    show_popup();
                    String date = "2009-10-10";
                    int i = Integer.parseInt(date.substring(0,4).trim())+2000;
                    result_http_client.append("INT : "+i+"\n\n");
                    String string = "{\n" +
                            "\"organisme\":1,\"province\":djnfjj,\"filiere\":3,\"promotion\":\"2020\"\n" +
                            "}";
                    Classtest.write_file_cache(getContext(),string);
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
                    */
                    /*+ Classtest.getJsonObjectById(getActivity(),"myjson.json",1).toString()

                     */
                    /*);
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
