package com.example.danielhidalgo.mylogin.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.danielhidalgo.mylogin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    //Form

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPasswordMatch;
    private Button buttonCancel;
    private Button buttonRegister;

    //Firebase

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        bindUI();

        mAuth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    private void bindUI(){
        editTextEmail = (EditText) findViewById(R.id.signin_email);
        editTextPassword = (EditText) findViewById(R.id.signin_password);
        editTextPasswordMatch = (EditText) findViewById(R.id.signin_password_match);
        buttonCancel = (Button) findViewById(R.id.button_cancel);
        buttonRegister = (Button) findViewById(R.id.button_register);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_cancel:
                goToLogIn();
                break;
            case R.id.button_register:
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String passwordMatch = editTextPasswordMatch.getText().toString();
                if(validate(email,password,passwordMatch)){
                    signIn(email, password);
                }


        }
    }

    private void goToLogIn(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean validate(String email, String password, String passwordMatch){
        if(!isValidEmail(email)){
            Toast.makeText(SignInActivity.this, "Invalid email.", Toast.LENGTH_LONG).show();
            return false;
        }else if(!isValidPassword(password)){
            Toast.makeText(SignInActivity.this, "Invalid password.", Toast.LENGTH_LONG).show();
            return false;
        }else if(!isPasswordMatched(password,passwordMatch)){
            Toast.makeText(SignInActivity.this, "Passwords aren't matched", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }

    }

    private boolean isValidEmail(String email){
        return !TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches(); // Validamos el formato de email
    }

    private boolean isValidPassword(String password){
        return password.length() >= 4;
    }

    private boolean isPasswordMatched(String password, String passwordMatch){
        return password.equals(passwordMatch);
    }

    private void signIn(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("EmailPassword", "createUserWithEmail: success");
                            Toast.makeText(SignInActivity.this, "Success Operation", Toast.LENGTH_LONG).show();
                            goToLogIn();
                        }else{
                            Log.w("EmailPassword", "createUserWithEmail: failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



}
