package com.example.orderapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {
    Context context;
    List<OrderData> orderDataList;
    OrderInterface orderInterface;

    public OrdersAdapter(Context context, List<OrderData> orderDataList, OrderInterface orderInterface) {
        this.context = context;
        this.orderDataList = orderDataList;
        this.orderInterface = orderInterface;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order,parent,false);
        OrdersViewHolder ordersViewHolder = new OrdersViewHolder(view);
        return ordersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        OrderData orderData = orderDataList.get(position);
        holder.description.setText(orderData.getDescription());
        holder.date.setText(orderData.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderInterface.onOrderClick(orderData);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderDataList.size();
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder {
    TextView description,date;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            description=itemView.findViewById(R.id.item_order_tv_description);
            date=itemView.findViewById(R.id.item_order_tv_date);
        }
    }

    public interface OrderInterface{
        void onOrderClick(OrderData orderData);
        }
    }

