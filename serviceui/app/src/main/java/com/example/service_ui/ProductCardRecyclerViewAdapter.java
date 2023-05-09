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
    private boolean isSingleView;
    private String comparedProdId = null;

    ProductCardRecyclerViewAdapter(List<Product> productList,
                                   FragmentActivity fragmentActivity,
                                   boolean isSingleView,
                                   String currentProdId) {
        this.productList = productList;
        imageRequester = ImageRequester.getInstance();
        this.fragmentActivity = fragmentActivity;
        this.isSingleView = isSingleView;
        this.comparedProdId = currentProdId;
    }

    @NonNull
    @Override
    public ProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ProductCardViewHolder(layoutView, this.fragmentActivity, this.isSingleView, comparedProdId);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductCardViewHolder holder, int position) {
        if (productList != null && position < productList.size()) {
            Product product = productList.get(position);

            if (product.productName.toCharArray().length > 20) {
                String substring = product.productName.substring(0,20) + "...";
                holder.productTitle.setText(substring);
            } else {
                holder.productTitle.setText(product.productName);
            }

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
