package com.mobilalk.computercomp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRETKEY = 1000;
    private SharedPreferences preferences;


    EditText editTextUserName, editTextPassword;
    SwitchMaterial switchAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        editTextUserName = findViewById(R.id.editTextLoginEmail);
        editTextPassword = findViewById(R.id.editTextLoginPassword);
        switchAdmin = findViewById(R.id.switchAdmin);
    }

    public void login(View view){

        String stringUsername = editTextUserName.getText().toString();
        String stringPassword = editTextPassword.getText().toString();
        boolean isAdmin = switchAdmin.isChecked();

        String msg = String.format("Bejelentkezett: %s jelszó: %s Admin: %b", stringUsername, stringPassword, isAdmin);
        Log.i(LOG_TAG, msg);
        //TODO hanincs regisztrálva áddob regisztrálni
    }

    public void register(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        registerIntent.putExtra("SECRETKEY", SECRETKEY );

        startActivity(registerIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", editTextUserName.getText().toString());
        editor.apply();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}