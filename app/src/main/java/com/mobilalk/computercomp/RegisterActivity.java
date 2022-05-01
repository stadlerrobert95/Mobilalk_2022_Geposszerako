package com.mobilalk.computercomp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private SharedPreferences preferences;
    EditText editTextEmail, editTextPassword, editTextPasswordAgain;
    String username, email, password, passwordAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle extrasBundle = getIntent().getExtras();
        int secretkey = extrasBundle.getInt("SECRETKEY");

        if (secretkey != 99) {
            finish();
        }

        editTextEmail = findViewById(R.id.editTextRegisterEmailAddress);
        editTextPassword = findViewById(R.id.editTextRegisterPassword);
        editTextPasswordAgain = findViewById(R.id.editTextRegisterPasswordAgain);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
    }

    public void cancel(View view) {
        finish();
    }

    public void register(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        passwordAgain = editTextPasswordAgain.getText().toString();
        StringBuilder msg = new StringBuilder();
        String[] split = email.split("@");
        username = split[0];

        if (!validateEmail(email)) {
            msg.append("Nem megfelelő email formátum!");
        }

        if (!password.equals(passwordAgain)){
            Log.i(LOG_TAG, String.format("Email: %s Password: %s PasswordAgain: %s %s", email, password, passwordAgain, username));
            if (msg.length() > 0){
                msg.append("\n");
            }
            msg.append("HIBA: A két jelszó nem egyezik meg!");
        }

        if (password.length() < 6 & password.equals(passwordAgain)){
            Log.i(LOG_TAG, String.format("Email: %s Password: %s PasswordAgain: %s %s", email, password, passwordAgain, username));
            if (msg.length() > 0){
                msg.append("\n");
            }
            msg.append("Túl rövid a jelszó! (minimum 6 karakter)");
        }

        if (password.length() >= 6 & password.equals(passwordAgain) & validateEmail(email)){
            Log.i(LOG_TAG, String.format("Email: %s Password: %s PasswordAgain: %s %s", email, password, passwordAgain, username));
            msg.append("Sikeres regisztráció!");
            finish();
        }
        toaster(msg);
    }

    private void toaster(CharSequence message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    private boolean validateEmail(String email){
        return Pattern.compile(getString(R.string.emailRegexPattern)).matcher(email).matches();
    }

    @Override
    protected void onStart() {
        email = preferences.getString("email","");
        editTextEmail.setText(email);
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
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}