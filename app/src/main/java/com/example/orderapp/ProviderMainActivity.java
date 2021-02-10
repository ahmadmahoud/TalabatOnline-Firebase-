package com.example.orderapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class ProviderMainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private List<OrderData> orderDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_main);
        recyclerView=findViewById(R.id.main_rv_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getOrders();
    }

    private void getOrders() {
        firestore.collection("Orders")
                .whereEqualTo("accept",false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error !=null){
                            String errorMessage = error.getLocalizedMessage();
                            Toast.makeText(ProviderMainActivity.this, errorMessage   , Toast.LENGTH_SHORT).show();
                            return;
                        }
                        orderDataList.clear();
                        for (int i = 0; i <value.getDocuments().size() ; i++) {
                            OrderData orderData =
                                    value.getDocuments().get(i).toObject(OrderData.class);
                            if (orderData.isFinished())continue;
                            orderDataList.add(orderData);
                            OrdersAdapter ordersAdapter =new OrdersAdapter(ProviderMainActivity.this,orderDataList,orderInterface);
                            recyclerView.setAdapter(ordersAdapter);
                        }
                    }
                });


    }
    OrdersAdapter.OrderInterface orderInterface =new OrdersAdapter.OrderInterface() {
        @Override
        public void onOrderClick(OrderData orderData) {
            Intent intent =new Intent(ProviderMainActivity.this,ProviderOrderDetailsActivity.class);
            intent.putExtra("OrderData",orderData);
            startActivity(intent);
        }
    };

    public void signOut() {
        firebaseAuth.signOut();
        if (firebaseAuth.getCurrentUser() == null) {
            // go to login screen
            Intent intent = new Intent(ProviderMainActivity.this, LoginActivity.class);
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
        } else if (id == R.id.item_my_orders) {
            openMyOrders();
        }else if (id == R.id.item_logout) {
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openMyOrders() {
        startActivity(new Intent(this, ProvidersMyOrdersActivity.class));
    }

    private void openProfile() {
        startActivity(new Intent(this, ProfileActivity.class));
    }
}