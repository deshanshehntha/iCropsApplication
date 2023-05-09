package com.example.service_ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service_ui.model.Order;

import javax.xml.parsers.FactoryConfigurationError;

public class AllOrderCardViewHolder extends RecyclerView.ViewHolder {
    public TextView orderId;
    public TextView date;
    public TextView status;
    public Order order;

    public AllOrderCardViewHolder(@NonNull View itemView, FragmentActivity fragmentActivity) {
        super(itemView);
        orderId = itemView.findViewById(R.id.ord_id);
        date = itemView.findViewById(R.id.date);
        status = itemView.findViewById(R.id.stat);

        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction =
                        fragmentActivity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, new ViewOrderFragment(order));
                transaction.commit();
            }
        });
    }
}