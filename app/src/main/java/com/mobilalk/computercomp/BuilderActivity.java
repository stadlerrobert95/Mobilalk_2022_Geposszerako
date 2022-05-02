package com.mobilalk.computercomp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.mobilalk.computercomp.databinding.ActivityBuilderBinding;

public class BuilderActivity extends AppCompatActivity {

    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();
    private static final String LOG_TAG = BuilderActivity.class.getName();
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityBuilderBinding binding;
    private SharedPreferences preferences;

    TextView textViewUsername, textViewEmail;
    String username, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBuilderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavigationView navView = findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);

        textViewUsername = headerView.findViewById(R.id.loggedInName);
        textViewEmail = headerView.findViewById(R.id.loggedInEmail);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        setSupportActionBar(binding.appBarBuilder.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_builder);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.builder, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_builder);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        username = preferences.getString("username", "");
        email = preferences.getString("email", "");
        textViewEmail.setText(email);
        textViewUsername.setText(username);
        super.onStart();
    }
}