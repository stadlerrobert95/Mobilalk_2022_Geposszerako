package com.mobilalk.computercomp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRETKEY = 1000;
    private static final int RC_SIGN_IN = 123;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    StringBuilder msg;

    EditText editTextUserName, editTextPassword;
    String username, email;
    SwitchMaterial switchAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle(getResources().getString(R.string.mainActivityName));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        msg = new StringBuilder();

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        editTextUserName = findViewById(R.id.editTextLoginEmail);
        editTextPassword = findViewById(R.id.editTextLoginPassword);
        switchAdmin = findViewById(R.id.switchAdmin);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        msg.delete(0,msg.length());
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(LOG_TAG, "Firebaseauth: " + account.getId());
                username = account.getDisplayName();
                email = account.getEmail();
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e){
                Log.w(LOG_TAG, "Google bejelentkezés nem sikerült",e);
            }
        }
    }

    public void firebaseAuthWithGoogle(String idToken){
        msg.delete(0,msg.length());
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                msg.append("Sikeres bejelentkezés!");
                toaster(msg);
                startBuilder();
            } else {
                msg.append("Sikertelen bejelentkezés!");
                toaster(msg);
            }
        }
        );
    }

    public void login(View view){

        String stringUsername = editTextUserName.getText().toString();
        String stringPassword = editTextPassword.getText().toString();
        boolean isAdmin = switchAdmin.isChecked();
        Log.i(LOG_TAG, String.format("Bejelentkezett: %s jelszó: %s Admin: %b", stringUsername, stringPassword, isAdmin));
        msg.delete(0,msg.length());
        mAuth.signInWithEmailAndPassword(stringUsername,stringPassword).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                msg.append("Sikeres bejelentkezés!");
                toaster(msg);
                setUsername(editTextUserName.getText().toString());
                startBuilder();
            } else {
                msg.append("Sikertelen bejelentkezés!");
                toaster(msg);
            }
        }
        );
        //TODO hanincs regisztrálva áddob regisztrálni
    }

    private void toaster(CharSequence message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    private void startBuilder(/**/){
        Intent intent = new Intent(this, PartListActivity.class);
        intent.putExtra("SECRETKEY", SECRETKEY);
        startActivity(intent);
    }

    public void register(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        registerIntent.putExtra("SECRETKEY", SECRETKEY );

        startActivity(registerIntent);
    }

    public void loginAsGuest(View view) {
        msg.delete(0,msg.length());
        mAuth.signInAnonymously().addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                msg.append("Anonim bejelentkezés sikeres!");
                toaster(msg);
                editTextUserName.setText("Anonymous");
                setUsername("Anonymous");
                startBuilder();
            } else {
                msg.append("Sikertelen bejelentkezés!");
                toaster(msg);
            }
        });
    }

    public void loginWithGoogle(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void setUsername(String email){
        String[] split = email.split("@");
        username = split[0];
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
        editor.putString("username", username);
        editor.putString("email", email);
        editor.apply();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}