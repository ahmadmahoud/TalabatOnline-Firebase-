package com.example.orderapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private Uri imageUri =null;
    CircleImageView circleImageView;
    ProgressBar progressBar;
    EditText editTextEmail, editTextUserName, editTextPhone;
    MaterialButton buttonUpdate;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        circleImageView = findViewById(R.id.profile_user_image);
        editTextEmail = findViewById(R.id.profile_et_user_email);
        editTextUserName = findViewById(R.id.profile_et_username);
        editTextPhone = findViewById(R.id.profile_et_phone);
        progressBar = findViewById(R.id.profile_progress_bar);
        buttonUpdate = findViewById(R.id.profile_btn_update);
        getUserData();
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }
        });
    }
    // 1- نجيب الداتا من الفاير ستور
    private void getUserData() {
        firestore.collection("Users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    UserData userData = task.getResult().toObject(UserData.class);
                    updateUi(userData);
                } else {
                    String errorMessage = Objects.requireNonNull(task.getException()).getLocalizedMessage();
                    Log.i(TAG, "onComplete: " + errorMessage);
                    Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    //   2- عشان نعرف نجيب الداتا من الفايرستور و معاها الصورة لازم نجيب الصورة الاول عنطريق مكتبة بيكاسو
    private void updateUi(UserData userData) {
        editTextEmail.setText(userData.getEmail());
        editTextUserName.setText(userData.getName());
        editTextPhone.setText(userData.getPhone());
        Picasso.get().load(userData.getImageUrl()).placeholder(R.drawable.user).into(circleImageView);
    }
    private void updateUserData() { //3- نبعت الداتا الجديدة (update data(Text Only)) عنطريقHashMap
        String name = editTextUserName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        if (name.isEmpty()) {
            editTextUserName.setError("Name Required");
            return;
        }
        if (phone.isEmpty()) {
            editTextPhone.setError("Phone Required");
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("phone", phone);
        firestore.collection("Users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProfileActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // 4- عشان نعرف نبعت الصورة الجديدة لازم نعمل 5 خطوات
    //___________________________________________________________________
            //1- نفتح الجالارى عن طريق (image-cropper library)
    // start picker to get image for cropping and then use the image in cropping activity
    public void openGallery() {
              CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }
    //2-Override onActivityResult method in your activity to get crop result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) ;

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                circleImageView.setImageURI(imageUri);
                uploadImage();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)  {
                Exception error = result.getError();
                error.getLocalizedMessage();
            }
        }
    }
    //3- نبعت الصورة الجديدة بال(uid) فى storageReference
    private void uploadImage() {
        String uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        storageReference.child("photos").child(uid).putFile(imageUri)
                //first child name of storage folder =>  name of picture (before) putFile
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ProfileActivity.this, "Image Uploaded Success", Toast.LENGTH_SHORT).show();
                        getImageUrl(uid);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    //4-نجيب ال(url) الصورة الجديدة من ال storageReference
    private void getImageUrl(String uid) {
        storageReference.child("photos").child(uid).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            String imageUrl = task.getResult().toString();
                            Log.i(TAG, "onComplete: "+imageUrl);
                            uploadUserUrl(imageUrl);
                        }else {
                            String errorMessage = Objects.requireNonNull(task.getException()).getLocalizedMessage();
                            Log.i(TAG, "onComplete: " + errorMessage);
                            Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    //5- نبعت الصورة الجديدة لل firestore عنطريق ال HashMap
    private void uploadUserUrl(String imageUrl) {
        HashMap<String,Object> map=new HashMap<>();
        map.put("imageUrl",imageUrl);
        firestore.collection("Users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProfileActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}