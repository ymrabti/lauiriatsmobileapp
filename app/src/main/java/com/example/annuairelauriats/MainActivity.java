package com.example.annuairelauriats;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.annuairelauriats.ui.home.Classtest.base64toImage;
import static com.example.annuairelauriats.ui.home.Classtest.getJsonObjectBycle;
import static com.example.annuairelauriats.ui.home.Classtest.id_connected;
import static com.example.annuairelauriats.ui.home.Classtest.images_file;
import static com.example.annuairelauriats.ui.home.Classtest.laureats;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;public static NavigationView navigationView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send,
                R.id.nav_proposnous,R.id.nav_stndrds,R.id.nav_aide,R.id.nav_signaler)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerLayout = navigationView.getHeaderView(0);
        CircleImageView image_main = headerLayout.findViewById(R.id.pdp_show);
        TextView nom_prenom = headerLayout.findViewById(R.id.usernameclc);
        TextView email = headerLayout.findViewById(R.id.email_top_navheader);

        JSONObject laurat_visitee ;
        try {
            laurat_visitee = getJsonObjectBycle(this,"id",id_connected,laureats);
            JSONObject image= getJsonObjectBycle(this,"laureat",id_connected,images_file);
            image_main.setImageBitmap(base64toImage(image.getString("image")));
            nom_prenom.setText(laurat_visitee.getString("nom"));
            nom_prenom.append(" ");nom_prenom.append(laurat_visitee.getString("prenom"));
            email.setText(laurat_visitee.getString("email"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    private void test(){}

}
