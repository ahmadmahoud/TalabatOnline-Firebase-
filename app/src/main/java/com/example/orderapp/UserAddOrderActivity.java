package com.example.orderapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class UserAddOrderActivity extends AppCompatActivity {
    EditText editTextDescription, editTextPhone, editTextFirstLocation, editTextLastLocation,
            editTextOrderDate, editTextOrderTime;
    MaterialButton buttonAdd;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ExceptionInInitializerError task;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_order);
        editTextDescription = findViewById(R.id.add_order_request_description);
        editTextPhone = findViewById(R.id.add_order_phone_number);
        editTextFirstLocation = findViewById(R.id.add_order_first_location);
        editTextLastLocation = findViewById(R.id.add_order_last_location);
        editTextOrderDate = findViewById(R.id.add_order_date);
        editTextOrderTime = findViewById(R.id.add_order_time);
        buttonAdd = findViewById(R.id.add_order_material_button);
        setDate();
        setTime();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromUi();
                finish();
            }
        });
    }

    private void getDataFromUi() {
        String description = editTextDescription.getText().toString();
        String phone = editTextPhone.getText().toString();
        String firstLocation = editTextFirstLocation.getText().toString();
        String lastLocation = editTextLastLocation.getText().toString();
        String date = editTextOrderDate.getText().toString();
        String time = editTextOrderTime.getText().toString();
        if (description.isEmpty() || phone.isEmpty() || firstLocation.isEmpty() || lastLocation.isEmpty()
                || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please Fill all Data", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = firebaseAuth.getCurrentUser().getUid();
        String orderId = userId + System.currentTimeMillis();
        OrderData orderData = new  OrderData(orderId,userId,description,phone,firstLocation,
                lastLocation,date,time,"","",false,false);
            uploadOrderData(orderData);
    }

    private void uploadOrderData(OrderData orderData) {
       firestore.collection("Orders")
               .document(orderData.getOrderId())
               .set(orderData)
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Toast.makeText(UserAddOrderActivity.this, "Order added Successfully", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(UserAddOrderActivity.this , AcceptScreen.class));
                       }else {
                           errorMessage();
                       }
                   }

               });
    }
     public void errorMessage(){
         String errorMessage= task.getException().getLocalizedMessage();
         Toast.makeText(UserAddOrderActivity.this, errorMessage , Toast.LENGTH_SHORT).show();
     }
     public void setDate(){
         editTextOrderDate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Calendar calendar = Calendar.getInstance();
                 int year = calendar.get(Calendar.YEAR);
                 int month = calendar.get(Calendar.MONTH);
                 int day = calendar.get(Calendar.DAY_OF_MONTH);
                 DatePickerDialog dialog = new DatePickerDialog(UserAddOrderActivity.this,dateSetListener,year,month,day);
                 dialog.getWindow();
                 dialog.show();
             }
         });
         dateSetListener =new DatePickerDialog.OnDateSetListener() {
             @Override
             public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                 month=month+1;
                 String date = dayOfMonth+" / "+month+" / "+year;
                 editTextOrderDate.setText(date);
             }
         };
     }
     public void setTime(){
         editTextOrderTime.setOnClickListener(new View.OnClickListener() {
             Calendar calendar = Calendar.getInstance();
             int hours =calendar.get(Calendar.HOUR);
             int minutes = calendar.get(Calendar.MINUTE);
             String amPm;
             @Override
             public void onClick(View v) {
                 TimePickerDialog timePickerDialog = new TimePickerDialog(UserAddOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                     @Override
                     public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                         if (hourOfDay>=12){
                             amPm="PM";
                         }else {
                             amPm="AM";
                         }
                         if (hourOfDay>12){
                             hourOfDay=hourOfDay-12;
                         }

                         editTextOrderTime.setText(hourOfDay + ":"+minute+" "+amPm);
                     }
                 },hours,minutes,false);
                 timePickerDialog.show();
             }

         });

     }

}