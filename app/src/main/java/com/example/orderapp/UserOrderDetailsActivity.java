package com.example.orderapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserOrderDetailsActivity extends AppCompatActivity {

    TextInputEditText editTextDescription, editTextPhone, editTextFirstLocation, editTextLastLocation,
            editTextOrderDate, editTextOrderTime,editTextOrderState,editTextOrderAccept;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    MaterialButton buttonOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_details);
        OrderData orderData= (OrderData) getIntent().getSerializableExtra("OrderData");

        editTextDescription = findViewById(R.id.order_details_request_description);
        editTextPhone = findViewById(R.id.order_details_phone_number);
        editTextFirstLocation = findViewById(R.id.order_details_first_location);
        editTextLastLocation = findViewById(R.id.order_details_last_location);
        editTextOrderDate = findViewById(R.id.order_details_date);
        editTextOrderTime = findViewById(R.id.order_details_time);
        editTextOrderState=findViewById(R.id.order_user_details_state);
        editTextOrderAccept=findViewById(R.id.order_details_is_accept);
        buttonOk = findViewById(R.id.order_details_material_button);

        editTextDescription.setText(orderData.getDescription());
        editTextPhone.setText(orderData.getPhone());
        editTextFirstLocation.setText(orderData.getFirstLocation());
        editTextLastLocation.setText(orderData.getLastLocation());
        editTextOrderDate.setText(orderData.getDate());
        editTextOrderTime.setText(orderData.getTime());
        editTextOrderState.setText(orderData.getState());
        if (orderData.isAccept()){
            editTextOrderAccept.setText(R.string.order_accept);
            editTextOrderAccept.setBackgroundColor(getResources().getColor(R.color.green));
        }
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserOrderDetailsActivity.this, UserMainActivity.class));
            }
        });
    }
}