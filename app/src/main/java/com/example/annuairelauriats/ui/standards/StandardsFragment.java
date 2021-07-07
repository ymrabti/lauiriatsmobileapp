package com.example.annuairelauriats.ui.standards;

import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

import static android.app.Activity.RESULT_OK;
import static com.example.annuairelauriats.ui.home.Classtest.getUniqueIMEIId;
import static com.example.annuairelauriats.ui.home.Classtest.ip_server;
import static com.example.annuairelauriats.ui.home.Classtest.ip_serverIP;
import static com.example.annuairelauriats.ui.home.Classtest.portBackend;

public class StandardsFragment extends Fragment {
    private TextView result_http_client,textView;private VideoView videoView;private EditText url;
    private ImageView imageView,imageViewm;private EditText password;boolean connexion ;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.standardscommunute, container, false);
        url = root.findViewById(R.id.editText1);password=root.findViewById(R.id.pssssss);
        url.setText(ip_server+"/laureat/id/8");password.setText("hyouri sama");
        videoView = root.findViewById(R.id.videoView);
        result_http_client=root.findViewById(R.id.result_http_client);
        textView=root.findViewById(R.id.textView2);
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

        //result_http_client.append(connexion+" con");
        /*if (!connexion){
            CountDownTimer countDownTimer = new CountDownTimer(5000,500) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    show_exitPopup();
                }
            };
            countDownTimer.start();
        }*/
        but_connect.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
        try {
            result_http_client.setText(getUniqueIMEIId());
        } catch (Exception e) {
            e.printStackTrace();
            result_http_client.append("ici"+e.toString());
        }
            }
        });
        return root;
    }
    private void show_exitPopup(){
        Dialog dialog = new AppCompatDialog(getActivity());
        dialog.setCancelable(false);dialog.setContentView(R.layout.popup_error_connection);
        dialog.findViewById(R.id.exit_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        dialog.show();
    }
    private void isPortOpen() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket();
                    InetSocketAddress intent= new InetSocketAddress(ip_serverIP,portBackend);
                    socket.connect(intent);
                    result_http_client.append("true\n");
                    socket.close();
                } catch(Exception ce){
                    ce.printStackTrace();
                    result_http_client.append("false\n");
                }
            }
        });
    }
    private void executeCommand(){
        Runtime runtime = Runtime.getRuntime();
        try
        {
            Process  mIpAddrProcess = runtime.exec("ping -c 5 "+ip_serverIP);
            int mExitValue = mIpAddrProcess.waitFor();
            result_http_client.append(mExitValue+" mExit\n");
            connexion= (mExitValue == 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();result_http_client.append(e.toString());
        }
        connexion= false;
    }
    private Boolean getLatency(){
        String pingCommand = "/system/bin/ping -c 5 "+ip_serverIP;
        String inputLine = "";
        try {
            Process process = Runtime.getRuntime().exec(pingCommand);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            inputLine = bufferedReader.readLine();
            while ((inputLine != null))
            {
                if (inputLine.contains("5 received")) {
                    break;
                }
                inputLine = bufferedReader.readLine();
            }
        }
        catch (Exception e){ e.printStackTrace(); }
        if (inputLine != null) {
            return inputLine.contains("5 received");
        }
        else{return false;}
    }




    private void OpenGallery(){
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
    /*private static String isNetworkAvailable(Context context) {
        if(context == null)  return "context null";
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return "cellulaire";
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return "wifi";
                    }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                        return "ethernet";
                    }
                }
            }

            else {

                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        return "network";
                    }
                } catch (Exception e) {e.printStackTrace();
                }
            }
        }
        return "manager null";
    }
    public void registerNetworkCallback() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();

            if (connectivityManager != null) {
                connectivityManager.registerNetworkCallback(builder.build(),new ConnectivityManager.NetworkCallback() {
                            @Override
                            public void onAvailable(Network network) {
                                result_http_client.append("connected\n");
                            }
                            @Override
                            public void onLost(Network network) {
                                result_http_client.append("lost\n");
                            }
                        }

                );
            }
            result_http_client.append("manager null\n");
        }catch (Exception e){
            result_http_client.append("sorry\n");
        }
    }
    private void sendPost() {
        AsyncTask.execute(new Runnable()  {
            @Override
            public void run() {
                try {
                    URL url = new URL("");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);conn.setDoInput(true);
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("valeur", 12);
                    jsonParam.put("id_matiere", 2);
                    jsonParam.put("id_etudiant", 1);
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());os.flush();os.close();
                    result_http_client.append("response code "+conn.getResponseCode()+"\n");
                    result_http_client.append("response message "+conn.getResponseMessage()+"\n");
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    result_http_client.append(e.toString());
                }
            }
        });
    }
    private void connecting_reste(final String url) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL laureatEndpoint = new URL(url);
                    HttpURLConnection myConnection = (HttpURLConnection) laureatEndpoint.openConnection();
                    myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");
                    result_http_client.append("");
                    result_http_client.append("RESPONSE CODE  :  "+myConnection.getResponseCode()+" \n\n");
                    if(myConnection.getResponseCode() == 200) {
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                        DataInputStream in = new DataInputStream(responseBody);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String strLine;
                        while ((strLine = br.readLine()) != null) {
                            result_http_client.append(strLine+"\n");
                        }

                    }
                    myConnection.disconnect();
                } catch (Exception e) {
                    result_http_client.append(e+" \nexception\n"+" \n\n");
                }
            }
        });
    }
    private String nom(){
        String list="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return "'" +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                "'";
    }
    private String email(){
        String list="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return "'" +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +"@"+
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +"."+
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                "'";
    }
    private String nu_tel(){
        String list="0123456789";
        return "'0" +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                list.charAt((int) num(0, list.length() - 1)) +
                "'";
    }
    private String genre(){
        String list="MF";
        return "'" + list.charAt((int) num(0, list.length() - 1)) + "'";
    }
    private String date (){
        return "'"+num(2017,2020)+"-"+num(1,12)+"-"+num(1,28)+"'";
    }
    private long num(int min,int max){
        return round(min+random()*(max-min));
    }
    private void script(){
        StringBuilder sql = new StringBuilder();
        for (int i=52;i<252;i++){
            result_http_client.append("UPDATE public.laureats\n" +
                    "\tSET  status="+num(1,4)+"\n" +
                    "\tWHERE id="+i+";");
            sql.append("UPDATE public.laureats\n" + "\tSET  status=").append(num(1, 4))
                    .append("\n").append("\tWHERE id=").append(i).append(";");
        }
        //write_file_data(getContexte(),sql.toString(),"sqloo.sql");
    }
    sql.append("INSERT INTO public.laureat_org(\n" + "\torg, laureat, en_cours, date_debut, fonction)\n" + "\tVALUES (")
                    .append(num(2, 5)).append(", ").append(i).append(", ").append(true).append(", ")
                    .append(date()).append(", ").append(nom()).append(");");

       sql.append("INSERT INTO public.laureats(\n" + "\tnom, prenom, gender, promotion, filiere, date_inscription" +
                    ", description, telephone, email, pass_word, role, actif)\n" + "\tVALUES ( ")
                    .append(nom()).append(", ").append(nom()).append(", ").append(genre())
                    .append(", ").append(num(2000, 2020)).append(", ").append(num(2, 5))
                    .append(", ").append(date ()).append(", ").append(nom()).append(", ")
                    .append(nom()).append(", ").append(email()).append(", ").append(nom())
                    .append(", ").append(1).append(", ").append(true).append(");");

            sql.append("UPDATE public.laureats\n" + "\tSET  status=").append(num(1, 4))
                    .append("\n").append("\tWHERE id=").append(i).append(";");*/
    /*RequestQueue requestQueue = Volley.newRequestQueue(context);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( url.getText().toString()
                    , new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        JSONObject image_obj = response.getJSONObject(0);
                        imageView.setImageBitmap(base64toImage(image_obj.getString("photo")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },null);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("laureat",8);jsonObject.put("IME","njjfjjfjjfjfjfjfjfj");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    ip_server + "/autres/insert_device", jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            result_http_client.setText(response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    result_http_client.setText(error.toString());
                }
            });
            requestQueue.add(jsonArrayRequest);requestQueue.add(jsonObjectRequest);
            JsonObjectRequest ExampleRequest = new JsonObjectRequest("",null,  new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    result_http_client.append(response.toString());
                }

            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    result_http_client.append(error.toString());
                }
            });
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("", new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    result_http_client.append(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    result_http_client.append(error.toString());
                }
            });
            JSONObject note = new JSONObject();
            note.put("valeur", 13);
            note.put("id_matiere", 7);
            note.put("id_etudiant", 4);
            RequestQueue ExampleRequestQueue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "", note,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            result_http_client.append(response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    result_http_client.append(error.toString());
                }
            });
            ExampleRequestQueue.add(jsonObjectRequest);*/
    /*Bitmap icon = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            String textToDraw = url.getText().toString();
            Bitmap icoon = getRoundedShape(get_Bitmap(getContext(),R.drawable.circle_shape));
            imageViewm.setImageBitmap(add_text(icoon,textToDraw));
            result_http_client.setText(is_file_exists(getContext(),url.getText().toString())+" file");
            Bitmap icon = resize_bitmap(((BitmapDrawable) imageView.getDrawable()).getBitmap());
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
}
