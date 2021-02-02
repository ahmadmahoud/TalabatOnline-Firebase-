package com.example.orderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    ProgressBar progressBar;
    TextInputEditText editTextEmail ,editTextUserName, editTextPhone;
    MaterialButton buttonUpdate;
    FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    FirebaseFirestore firestore =FirebaseFirestore.getInstance();
    private static final String TAG = "ProfileActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        circleImageView=findViewById(R.id.profile_user_image);
        editTextEmail=findViewById(R.id.profile_et_user_email);
        editTextUserName=findViewById(R.id.profile_et_username);
        editTextPhone=findViewById(R.id.profile_et_phone);
        progressBar=findViewById(R.id.profile_progress_bar);
        buttonUpdate=findViewById(R.id.profile_btn_update);
        getUserData();
    }

    private void getUserData() {
            firestore.collection("Users")
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                    UserData userData = task.getResult().toObject(UserData.class);
                    updateUi(userData);
                    }else {

                    }

                }
            });
    }

    private void updateUi(UserData userData) {
        editTextEmail.setText(userData.getEmail());
        editTextUserName.setText(userData.getName());
        editTextPhone.setText(userData.getPhone());
        Picasso.get().load(userData.getImageUrl()).placeholder(R.drawable.user).into(circleImageView);
    }
}