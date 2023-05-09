package com.example.service_ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service_ui.model.OrderLine;
import com.example.service_ui.model.Product;

import java.util.List;

public class OrderCardRecyclerViewAdapter extends RecyclerView.Adapter<OrderCardViewHolder> {
    private boolean isCustomerView;
    private FragmentManager fragmentManager;
    private List<OrderLine> orderLines;
    OrderCardRecyclerViewAdapter(List<OrderLine> orderLines, FragmentManager fragmentManager, boolean isCustomerView) {
        this.fragmentManager = fragmentManager;
        this.orderLines = orderLines;
        this.isCustomerView = isCustomerView;
    }

    @NonNull
    @Override
    public OrderCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false);
        return new OrderCardViewHolder(layoutView, this.isCustomerView);
    }


    @Override
    public void onBindViewHolder(@NonNull OrderCardViewHolder holder, int position) {
        if (orderLines != null && position < orderLines.size()) {
            holder.fragmentManager = fragmentManager;
            OrderLine orderLine = orderLines.get(position);
            holder.orderLineId = orderLine.getOrderLineId();

            holder.productTitle.setText(orderLine.getProductName());

            if (orderLine.getProductName() != null && orderLine.getProductName().toCharArray().length > 20) {
                String substring = orderLine.getProductName().substring(0,20) + "...";
                holder.productTitle.setText(substring);
            } else {
                holder.productTitle.setText(orderLine.getProductName());
            }

            if (orderLine.getOrderLineStatus() == null) {
                holder.orderLineStatus.setText("Not Created");
            } else {
                holder.orderLineStatus.setText(orderLine.getOrderLineStatus());
            }

            holder.productPrice.setText(orderLine.getPrice());
            holder.qty.setText("Qty :" + orderLine.getQuantity());
        }
    }

    @Override
    public int getItemCount() {
        return orderLines.size();
    }
}
