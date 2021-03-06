package com.mobilalk.computercomp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final int SECRETKEY = 1000;
    private SharedPreferences preferences;
    EditText editTextEmail, editTextPassword, editTextPasswordAgain;
    String username, email, password, passwordAgain;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle extrasBundle = getIntent().getExtras();
        int secretkey = extrasBundle.getInt("SECRETKEY");

        if (secretkey != 1000) {
            finish();
        }

        editTextEmail = findViewById(R.id.editTextRegisterEmailAddress);
        editTextPassword = findViewById(R.id.editTextRegisterPassword);
        editTextPasswordAgain = findViewById(R.id.editTextRegisterPasswordAgain);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
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
            msg.append("Nem megfelel?? email form??tum!");
        }

        if (!password.equals(passwordAgain)){
            Log.i(LOG_TAG, String.format("Email: %s Password: %s PasswordAgain: %s %s", email, password, passwordAgain, username));
            if (msg.length() > 0){
                msg.append("\n");
            }
            msg.append("HIBA: A k??t jelsz?? nem egyezik meg!");
        }

        if (password.length() < 6 & password.equals(passwordAgain)){
            Log.i(LOG_TAG, String.format("Email: %s Password: %s PasswordAgain: %s %s", email, password, passwordAgain, username));
            if (msg.length() > 0){
                msg.append("\n");
            }
            msg.append("T??l r??vid a jelsz??! (minimum 6 karakter)");
        }

        if (password.length() >= 6 & password.equals(passwordAgain) & validateEmail(email)){
            Log.i(LOG_TAG, String.format("Email: %s Password: %s PasswordAgain: %s %s", email, password, passwordAgain, username));
            msg.append("Sikeres regisztr??ci??!");
        }
        toaster(msg);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startBuilder();
                } else {
                    msg.append("Sikertelen regisztr??ci??!");
                    toaster(msg);
                }
            }
        });

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

    private void startBuilder(/**/){
        Intent intent = new Intent(this, PartListActivity.class);
        intent.putExtra("SECRETKEY", SECRETKEY);
        startActivity(intent);
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
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("username", username);
        editor.apply();
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}