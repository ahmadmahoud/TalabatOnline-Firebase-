package com.example.orderapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    //initialisation editText
    private EditText editTextEmail, editTextPassword, editTextConfirmPassword, editTextName, editTextPhone;
    //initialisation  firebase to create email with password
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // initialisation StorageReference to can upload imageProfile
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    // initialisation FirebaseFireStore to can upload UserData
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    //initialisation User Data
    private CircleImageView imageProfile;
    private Uri imageUri = null;
    private String email = "";
    private String password = "";
    private String confirmPassword = "";
    private String name = "";
    private String phone = "";
    private RadioButton radioButton;
    private boolean provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextEmail = findViewById(R.id.register_et_email);
        editTextPassword = findViewById(R.id.register_et_password);
        editTextConfirmPassword = findViewById(R.id.register_et_confirm_password);
        imageProfile = findViewById(R.id.register_image);
        editTextName = findViewById(R.id.register_et_name);
        editTextPhone = findViewById(R.id.register_et_phone);
        radioButton = findViewById(R.id.radioButton_provider);

    }

    public void register(View view) {
    //Initialisation register fields (email, password, confirm password, name, phone)
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        confirmPassword = editTextConfirmPassword.getText().toString().trim();
        name = editTextName.getText().toString().trim();
        phone = editTextPhone.getText().toString().trim();

        // Initialisation radioButton for navigation (client Or Provider)
        provider = radioButton.isChecked();
        // check validation
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please Fill Data", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "The Passwords not matching", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageUri == null) {
            Toast.makeText(this, "Please Select image", Toast.LENGTH_SHORT).show();
            return;
        }
        //create user by email and password and connect database
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(RegisterActivity.this, "user Register Success", Toast.LENGTH_SHORT).show();
                            uploadImage();
                        } else {
                            String errorMessage = Objects.requireNonNull(task.getException()).getLocalizedMessage();
                            Log.i(TAG, "onComplete: " + errorMessage);
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void navigate() {
        if (provider){
            startActivity(new Intent(RegisterActivity.this, UserMainActivity.class));
        }else{
            startActivity(new Intent(RegisterActivity.this, ProviderMainActivity.class));
        }
        finish();
    }
    // 4- عشان نعرف نبعت الصورة الجديدة لازم نعمل 5 خطوات
    //___________________________________________________________________
    //1- نفتح الجالارى عن طريق (image-cropper library)
    // start picker to get image for cropping and then use the image in cropping activity
    public void openGallery(View view) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent,1);
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }
    //2-Override onActivityResult method in your activity to get crop result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) ;
//        imageUri =data.getData();
//        imageProfile.setImageURI(imageUri);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                imageProfile.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.getLocalizedMessage();
            }
        }
    }
    //3- نبعت الصورة الجديدة بال(uid) فى storageReference
    private void uploadImage() {
        String uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        storageReference.child("photos").child(uid).putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(RegisterActivity.this, "Image Uploaded Success", Toast.LENGTH_SHORT).show();
                        getImageUrl(uid);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
//        String uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
//        storageReference.child("photos").child(uid).putFile(imageUri)  //first child name of storage folder =>  name of picture (before) putFile
//        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(RegisterActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
//                } else {
//                    String errorMessage = Objects.requireNonNull(task.getException()).getLocalizedMessage();
//                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
    //4-نجيب ال(url) الصورة الجديدة من ال storageReference
    private void getImageUrl(String uid) {
        storageReference.child("photos").child(uid).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String imageUrl = task.getResult().toString();
                            Log.i(TAG, "onComplete: " + imageUrl);
                            uploadUserData(imageUrl);
                        } else {
                            String errorMessage = Objects.requireNonNull(task.getException()).getLocalizedMessage();
                            Log.i(TAG, "onComplete: " + errorMessage);
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    //5- كدة احنا جاهزين ب URL الصورة خلاص و نبعتها لل firestore مع ال UserData فى الكونستراكتور
    private void uploadUserData(String imageUrl) {
        UserData userData = new UserData(email, name, phone, imageUrl,provider);//ctrl + p
        firestore.collection("Users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        navigate();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

}