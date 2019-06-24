package org.aossie.agoraandroid.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import org.aossie.agoraandroid.R;

public class HomeActivity extends AppCompatActivity {
    androidx.appcompat.widget.Toolbar toolbar;
    NavigationView navView;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);

        Navigation.findNavController(this, R.id.fragment);// navController

        NavigationUI.setupWithNavController(navView, Navigation.findNavController(this, R.id.fragment));

        NavigationUI.setupActionBarWithNavController(this, Navigation.findNavController(this, R.id.fragment),
                drawerLayout);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.fragment),
                drawerLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }



}
