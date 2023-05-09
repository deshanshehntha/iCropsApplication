package com.example.service_ui;

import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class OrderCardViewHolder extends RecyclerView.ViewHolder {
    public TextView productTitle;
    public TextView productPrice;

    public String productId;
    public String orderLineId;
    public TextView qty;
    public TextView orderLineStatus;
    public FragmentManager fragmentManager;

    public OrderCardViewHolder(@NonNull View itemView, boolean isCustomerView) {
        super(itemView);
        productTitle = itemView.findViewById(R.id.product_title);
        productPrice = itemView.findViewById(R.id.description);
        qty = itemView.findViewById(R.id.price);
        orderLineStatus = itemView.findViewById(R.id.ol_status);

        if (!isCustomerView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment newFragment = new OrderStatusChanger(orderLineId);
                    newFragment.show(fragmentManager, "status");
                }
            });
        }
    }


}
