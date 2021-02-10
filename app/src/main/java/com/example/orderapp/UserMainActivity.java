package com.example.orderapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

//1- connect firebase :Tools >firebase
//2- authentication : email and password authentication > connect firebase >choose the database and connect
//3-enable email authentication
public class UserMainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    List<OrderData> orderDataList = new ArrayList<>();
    String uid;
    RecyclerView recyclerView;
    OrdersAdapter ordersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uid = firebaseAuth.getCurrentUser().getUid();
        recyclerView = findViewById(R.id.main_rv_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserMainActivity.this));
        getOrders();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() == null) {
            // go to login screen
            Intent intent = new Intent(UserMainActivity.this, LoginActivity.class);
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
            Intent intent = new Intent(UserMainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_profile) {
            openProfile();
        } else if (id == R.id.item_logout) {
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openProfile() {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    public void addNewOrder(View view) {
        startActivity(new Intent(this, UserAddOrderActivity.class));
    }

    public void getOrders() {
        firestore.collection("Orders")
                .whereEqualTo("userId", uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            String errorMessage = error.getLocalizedMessage();
                            Toast.makeText(UserMainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (int i = 0; i < value.getDocuments().size(); i++) {
                            OrderData orderData =
                                    value.getDocuments().get(i).toObject(OrderData.class);
                            orderDataList.add(orderData);
                            OrdersAdapter ordersAdapter = new OrdersAdapter(UserMainActivity.this, orderDataList, orderInterface);
                            recyclerView.setAdapter(ordersAdapter);
                        }

                        // ordersAdapter.notifyDataSetChanged();
                    }
                });
    }

    OrdersAdapter.OrderInterface orderInterface = new OrdersAdapter.OrderInterface() {
        @Override
        public void onOrderClick(OrderData orderData) {
            Intent intent = new Intent(UserMainActivity.this, UserOrderDetailsActivity.class);
            intent.putExtra("OrderData", orderData);
            startActivity(intent);
        }
    };

}