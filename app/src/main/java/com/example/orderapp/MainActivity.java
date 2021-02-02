package com.example.orderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    public void signOut() {
        firebaseAuth.signOut();
        if (firebaseAuth.getCurrentUser() == null) {
            // go to login screen
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.item_profile){
            openProfile();
        }else if(id==R.id.item_logout){
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openProfile() {
        startActivity(new Intent(this,ProfileActivity.class));
    }
}