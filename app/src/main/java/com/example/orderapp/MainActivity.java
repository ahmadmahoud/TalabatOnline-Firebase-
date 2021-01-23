package com.example.orderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

//1- connect firebase :Tools >firebase
//2- authentication : email and password authentication > connect firebase >choose the database and connect
//3-enable email authentication
public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() == null) {
            // go to login screen
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();

    }

    public void signOut(View view) {
        firebaseAuth.signOut();
        if (firebaseAuth.getCurrentUser() == null) {
            // go to login screen
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}