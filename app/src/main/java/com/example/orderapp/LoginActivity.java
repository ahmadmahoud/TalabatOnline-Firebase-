package com.example.orderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText editTextEmail,editTextPassword;
    FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail=findViewById(R.id.login_et_email);
        editTextPassword=findViewById(R.id.login_et_password);
    }

    public void login(View view) {
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();

        if(email.isEmpty()||password.isEmpty()){
            Toast.makeText(this, "Please Fill Data", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else {
                    String errorMessage= Objects.requireNonNull(task.getException()).getLocalizedMessage();
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onComplete: "+errorMessage);
                }
            }
        });

    }

    public void openRegister(View view) {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    public void openPhoneRegister(View view) {
        Intent intent = new Intent(LoginActivity.this,PhoneSignUpActivity.class);
        startActivity(intent);
    }
}