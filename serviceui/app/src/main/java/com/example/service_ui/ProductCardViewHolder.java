package com.example.service_ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.material.checkbox.MaterialCheckBox;

public class ProductCardViewHolder extends RecyclerView.ViewHolder {

    public NetworkImageView productImage;
    public TextView productTitle;
    public TextView productPrice;
    private FragmentActivity fragmentActivity;
    public String productId;

    public ProductCardViewHolder(@NonNull View itemView, FragmentActivity fragmentActivity) {
        super(itemView);
        this.fragmentActivity = fragmentActivity;
        productImage = itemView.findViewById(R.id.product_image);
        productTitle = itemView.findViewById(R.id.product_title);
        productPrice = itemView.findViewById(R.id.product_price);
        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewProductFragment viewProductFragment =
                        getViewProductFragment(productId, "ssss");

                FragmentTransaction transaction =
                        fragmentActivity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, viewProductFragment);

                if (true) {
                    transaction.addToBackStack("productView");
                }

                transaction.commit();
            }
        });
    }

    private ViewProductFragment getViewProductFragment(String productId, String customerId) {
        ViewProductFragment fragment = new ViewProductFragment();
        Bundle args = new Bundle();
        args.putString("productId", productId);
        args.putString("customerId", customerId);
        fragment.setArguments(args);
        return fragment;
    }

}
