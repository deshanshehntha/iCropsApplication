package com.example.service_ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service_ui.model.Order;
import com.example.service_ui.model.OrderLine;

import java.util.List;

public class AllOrderCardRecyclerViewAdapter extends RecyclerView.Adapter<AllOrderCardViewHolder> {

    private FragmentActivity fragmentActivity;
    private List<Order> orders;
    AllOrderCardRecyclerViewAdapter(List<Order> orders, FragmentActivity fragmentActivity) {
        this.orders = orders;
        this.fragmentActivity = fragmentActivity;
    }
    @NonNull
    @Override
    public AllOrderCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_card, parent, false);
        return new AllOrderCardViewHolder(layoutView, fragmentActivity);
    }
    @Override
    public void onBindViewHolder(@NonNull AllOrderCardViewHolder holder, int position) {
        if (orders != null && position < orders.size()) {
            Order order = orders.get(position);
            holder.orderId.setText(order.getOrderId());
            holder.date.setText(order.getOrderedDate());
            holder.status.setText(order.getOrderStatus());
            holder.order = order;
        }
    }
    @Override
    public int getItemCount() {
        return orders.size();
    }
}