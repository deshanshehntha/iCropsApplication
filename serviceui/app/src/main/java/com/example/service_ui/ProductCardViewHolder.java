package com.example.service_ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

public class ProductCardViewHolder extends RecyclerView.ViewHolder {
    public NetworkImageView productImage;
    public TextView productTitle;
    public TextView productPrice;
    private FragmentActivity fragmentActivity;
    public String productId;
    private boolean isSingleView;
    public String comparedProdId;


    public ProductCardViewHolder(@NonNull View itemView,
                                 FragmentActivity fragmentActivity,
                                 boolean isSingleView, String comparedProdId) {
        super(itemView);
        this.fragmentActivity = fragmentActivity;
        this.isSingleView = isSingleView;
        productImage = itemView.findViewById(R.id.product_image);
        productTitle = itemView.findViewById(R.id.product_title);
        productPrice = itemView.findViewById(R.id.description);

        if (isSingleView) {
            this.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    fragmentActivity.getSupportFragmentManager().popBackStack();

                    ExplanationFragment explanationFragment =
                            getRecommendationExplanationFragment(comparedProdId, productId);


                    FragmentTransaction transaction =
                            fragmentActivity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.container, explanationFragment);
                    fragmentActivity.getSupportFragmentManager().popBackStack();

                    transaction.addToBackStack("rec_explanation");
                    transaction.commit();
                    return true;
                }
            });
        }

        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewProductFragment viewProductFragment =
                        getViewProductFragment(productId, "ssss");

                FragmentTransaction transaction =
                        fragmentActivity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, viewProductFragment);
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


    private ExplanationFragment getRecommendationExplanationFragment(String currentProdId, String recProdId) {
        ExplanationFragment fragment = new ExplanationFragment();
        Bundle args = new Bundle();
        args.putString("currentProdId", currentProdId);
        args.putString("recProdId", recProdId);
        fragment.setArguments(args);
        return fragment;
    }
}
