package com.example.service_ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service_ui.model.Product;

import java.util.List;

/**
 * Adapter used to show a simple grid of products.
 */
public class ProductCardRecyclerViewAdapter extends RecyclerView.Adapter<ProductCardViewHolder> {

    private List<Product> productList;
    private ImageRequester imageRequester;
    private FragmentActivity fragmentActivity;

    ProductCardRecyclerViewAdapter(List<Product> productList, FragmentActivity fragmentActivity) {
        this.productList = productList;
        imageRequester = ImageRequester.getInstance();
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public ProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ProductCardViewHolder(layoutView, this.fragmentActivity);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductCardViewHolder holder, int position) {
        if (productList != null && position < productList.size()) {
            Product product = productList.get(position);
            holder.productTitle.setText(product.productName);
            holder.productPrice.setText(product.price);
            holder.productId = product.productId;
            imageRequester.setImageFromUrl(holder.productImage, product.imageLink);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
