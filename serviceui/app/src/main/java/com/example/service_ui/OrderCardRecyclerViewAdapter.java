package com.example.service_ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service_ui.model.OrderLine;
import com.example.service_ui.model.Product;

import java.util.List;

public class OrderCardRecyclerViewAdapter extends RecyclerView.Adapter<OrderCardViewHolder> {

    private List<OrderLine> orderLines;
    OrderCardRecyclerViewAdapter(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    @NonNull
    @Override
    public OrderCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false);
        return new OrderCardViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull OrderCardViewHolder holder, int position) {
        if (orderLines != null && position < orderLines.size()) {
            OrderLine orderLine = orderLines.get(position);
            holder.productTitle.setText(orderLine.getProductName());
            holder.productPrice.setText(orderLine.getPrice());
            holder.qty.setText(orderLine.getQuantity());
        }
    }

    @Override
    public int getItemCount() {
        return orderLines.size();
    }
}
