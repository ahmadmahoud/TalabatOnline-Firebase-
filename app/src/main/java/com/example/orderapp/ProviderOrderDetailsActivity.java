package com.example.orderapp;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProviderOrderDetailsActivity extends AppCompatActivity {
    TextInputEditText editTextDescription, editTextPhone, editTextFirstLocation, editTextLastLocation,
            editTextOrderDate, editTextOrderTime,editTextOrderState,editTextOrderAccept;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    MaterialButton buttonAccept,buttonUpdate;
    CheckBox checkBoxFinished;
    OrderData orderData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_order_ditails);
        orderData= (OrderData) getIntent().getSerializableExtra("OrderData");

        editTextDescription = findViewById(R.id.order_details_request_description);
        editTextPhone = findViewById(R.id.order_details_phone_number);
        editTextFirstLocation = findViewById(R.id.order_details_first_location);
        editTextLastLocation = findViewById(R.id.order_details_last_location);
        editTextOrderDate = findViewById(R.id.order_details_date);
        editTextOrderTime = findViewById(R.id.order_details_time);
        editTextOrderState = findViewById(R.id.order_details_state);
        editTextOrderAccept = findViewById(R.id.order_details_is_accept);
        buttonAccept = findViewById(R.id.provider_order_details_accept_material_button);
        buttonUpdate=findViewById(R.id.provider_order_details_update_material_button);
        checkBoxFinished =findViewById(R.id.cb_provider_order_details_is_finish);

        editTextDescription.setText(orderData.getDescription());
        editTextPhone.setText(orderData.getPhone());
        editTextFirstLocation.setText(orderData.getFirstLocation());
        editTextLastLocation.setText(orderData.getLastLocation());
        editTextOrderDate.setText(orderData.getDate());
        editTextOrderTime.setText(orderData.getTime());
        editTextOrderState.setEnabled(orderData.isAccept());
        editTextOrderState.setText(orderData.getState());

        checkAccept();
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptOrder();
            }
        });
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrder();

            }
        });
    }
    private void acceptOrder() {
        Map<String,Object> map =new HashMap<>();
        map.put("providerId",firebaseAuth.getCurrentUser().getUid());
        map.put("accept",true);
        firestore.collection("Orders")
                .document(orderData.getOrderId())
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProviderOrderDetailsActivity.this, "Order Accepted", Toast.LENGTH_SHORT).show();
                        editTextOrderState.setEnabled(orderData.isAccept());
                        editTextOrderAccept.setText(R.string.The_Order_Accepted);
                        buttonAccept.setVisibility(View.GONE);
                    }
                });
    }
    private void updateOrder() {
        String state = editTextOrderState.getText().toString().trim();
        if(state.isEmpty()){
            Toast.makeText(this, "please write the State", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String,Object> map =new HashMap<>();
        map.put("state",state);
        map.put("finish",checkBoxFinished.isChecked());
        map.put("finished",true);
        firestore.collection("Orders")
                .document(orderData.getOrderId())
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProviderOrderDetailsActivity.this, "Order Updated", Toast.LENGTH_SHORT).show();
                        if (checkBoxFinished.isChecked()){
                            checkBoxFinished.setVisibility(View.GONE);

                        }
                    }
                });
    }

    private void checkAccept() {
        if(orderData.isAccept()){
            buttonAccept.setVisibility(View.GONE);
        }else {
            buttonAccept.setVisibility(View.VISIBLE);
        }
    }
}
    
