package com.example.orderapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    //initialisation editText & firebase
    private EditText editTextEmail,editTextPassword,editTextConfirmPassword;
    private final FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private CircleImageView imageProfile;
    private Uri imageUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextEmail=findViewById(R.id.register_et_email);
        editTextPassword=findViewById(R.id.register_et_password);
        editTextConfirmPassword=findViewById(R.id.register_et_confirm_password);
        imageProfile=findViewById(R.id.image_profile);
    }

    public void openGallery(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }
    private void uploadImage(){
        String uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        storageReference.child("photos").child(uid).putFile(imageUri)  //first child name of storage folder =>  name of picture (before) putFile
        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    String errorMessage = Objects.requireNonNull(task.getException()).getLocalizedMessage();
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null);
        imageUri =data.getData();
        imageProfile.setImageURI(imageUri);
    }

    public void register(View view) {
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();
        String confirmPassword=editTextConfirmPassword.getText().toString().trim();
        // check validation
        if (email.isEmpty()||password.isEmpty()||confirmPassword.isEmpty()){
            Toast.makeText(this, "Please Fill Data", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)){
            Toast.makeText(this, "The Passwords not matching", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageUri == null){
            Toast.makeText(this, "Please Select image", Toast.LENGTH_SHORT).show();
            return;
        }
        //create user by email and password and connect database
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "user Register Success", Toast.LENGTH_SHORT).show();
                            uploadImage();
                        }else {
                            String errorMessage = Objects.requireNonNull(task.getException()).getLocalizedMessage();
                            Log.i(TAG, "onComplete: "+ errorMessage);
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}