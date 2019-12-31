package com.example.annuairelauriats;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.annuairelauriats.ui.gallery.GalleryFragment;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import static com.example.annuairelauriats.ui.gallery.GalleryFragment.to_map;
import static com.example.annuairelauriats.ui.home.HomeFragment.edit_profile;
import static com.example.annuairelauriats.ui.home.HomeFragment.statut;
import static com.example.annuairelauriats.ui.slideshow.SlideshowFragment.popup;
import static com.example.annuairelauriats.ui.slideshow.SlideshowFragment.to_list;

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
                R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_goto_map).setVisible(false);
        menu.findItem(R.id.action_filter_new).setVisible(false);
        menu.findItem(R.id.action_goto_liste).setVisible(false);
        menu.findItem(R.id.action_edit_profile).setVisible(false);
        menu.findItem(R.id.action_filter_new_map).setVisible(false);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter_new:{
                GalleryFragment.popup();
                return true;
            }
            case R.id.action_goto_map:{
                to_map();
                return true;
            }
            case R.id.action_goto_liste:{
                to_list();
                return true;
            }
            case R.id.action_filter_new_map:{
                popup();
                return true;
            }
            case R.id.action_edit_profile:{
                edit_profile();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }


}
