package com.example.orderapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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

public class ProvidersMyOrdersActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private List<OrderData> orderDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_providers_my_orders);
        recyclerView = findViewById(R.id.main_rv_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getOrders();
    }

    private void getOrders() {
        firestore.collection("Orders")
                .whereEqualTo("providerId", firebaseAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            String errorMessage = error.getLocalizedMessage();
                            Toast.makeText(ProvidersMyOrdersActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        orderDataList.clear();
                        for (int i = 0; i < value.getDocuments().size(); i++) {
                            OrderData orderData =
                                    value.getDocuments().get(i).toObject(OrderData.class);
                            orderDataList.add(orderData);
                            OrdersAdapter ordersAdapter = new OrdersAdapter(ProvidersMyOrdersActivity.this, orderDataList, orderInterface);
                            recyclerView.setAdapter(ordersAdapter);
                        }
                    }
                });
    }

    OrdersAdapter.OrderInterface orderInterface = new OrdersAdapter.OrderInterface() {
        @Override
        public void onOrderClick(OrderData orderData) {
            Intent intent = new Intent(ProvidersMyOrdersActivity.this, ProviderOrderDetailsActivity.class);
            intent.putExtra("OrderData", orderData);
            startActivity(intent);
        }
    };
}