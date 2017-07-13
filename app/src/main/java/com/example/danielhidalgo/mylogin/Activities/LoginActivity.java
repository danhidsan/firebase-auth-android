package com.example.danielhidalgo.mylogin.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.danielhidalgo.mylogin.R;
import com.example.danielhidalgo.mylogin.utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class LoginActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;

    private EditText editTextEmail;
    private EditText editTextPassword;

    private Switch switchRemenber;

    private Button buttonLogin;

    private Bundle bundle;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        bundle =  getIntent().getExtras();

        bindUI();
        setCredentialsIfExist();

        mAuthListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser(); // FiresbaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Log.i("SESION", "sesion iniciada con email:" + user.getEmail());
                }else{
                    Log.i("SESION", "sesion cerrada");
                }
            }
        };


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                if(validation(email, password)){
                    logIn(email, password);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_signIn:
                goToSignIn();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    private void bindUI() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        switchRemenber = (Switch) findViewById(R.id.switchRemenber);

        if (bundle != null){
            if ((boolean) bundle.get("checked") == true) {
                switchRemenber.setChecked(true);
            }
        }

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
    }

    private boolean validation(String email, String password){
        if(!isValidEmail(email)){
            Toast.makeText(this, "Email is not valid, please try again",Toast.LENGTH_LONG).show();
            return false;
        }else if (!isValidPassword(password)){
            Toast.makeText(this, "Password is not valid, 4 characters or more, please try again", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }


    private void saveOnPreferences(String email, String password){
        if(switchRemenber.isChecked()){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", email);
            editor.putString("pass", password);
            editor.apply();
        }
    }

    private boolean isValidEmail(String email){
        return !TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches(); // Validamos el formato de email
    }

    private boolean isValidPassword(String password){
        return password.length() >= 4;
    }

    private void goToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToSignIn(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    private void setCredentialsIfExist(){
        String email = Util.getUserMailPrefs(sharedPreferences);
        String password = Util.getUserPassPrefs(sharedPreferences);
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            editTextEmail.setText(email);
            editTextPassword.setText(password);
        }
    }

    private void logIn(String email, String password){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("EmailPassword", "signInWithEmail: success" );

                    FirebaseUser user = mAuth.getCurrentUser();
                    saveOnPreferences(user.getEmail(), editTextPassword.getText().toString());
                    goToMain();
                }else{
                    Log.w("EmailPassword", "signInWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
