package com.example.orderapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText editTextEmail,editTextPassword;
    FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
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
                 getUserData();
                }else {
                    String errorMessage= Objects.requireNonNull(task.getException()).getLocalizedMessage();
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onComplete: "+errorMessage);
                }
            }
        });

    }

    private void getUserData() {
        firestore.collection("Users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserData userData= documentSnapshot.toObject(UserData.class);
                        navigate(userData.isProvider());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void navigate(boolean provider) {
        if (provider){
            startActivity(new Intent(LoginActivity.this,ProviderMainActivity.class) );
        }else {
            startActivity(new Intent(LoginActivity.this,UserMainActivity.class));
        }
        finish();
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