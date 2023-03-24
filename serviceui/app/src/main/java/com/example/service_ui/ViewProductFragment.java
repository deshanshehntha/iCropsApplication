package com.example.service_ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.service_ui.constants.Constants;
import com.example.service_ui.model.OrderLine;
import com.example.service_ui.model.Product;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

public class ViewProductFragment extends Fragment {
    private EditText qtyText;
    private String productId;
    private List<Product> products;
    private SharedPreferences sharedpreferences;

    private BottomNavigationView bottomNavigationView;

    public ViewProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getString("productId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_product, container, false);
        bottomNavigationView = container.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);

        sharedpreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String preferredSupermarketId = sharedpreferences.getString(Constants.SHARED_PREF_SUPERMARKET_ID, null);

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        String url = Constants.HOST + Constants.PRODUCT_URI + "/" + productId;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Product product = gson.fromJson(response, Product.class);
                        TextView productTitle = view.findViewById(R.id.product_title_view);
                        productTitle.setText(product.productName);

                        TextView productDescription = view.findViewById(R.id.product_description_view);

                        if (product.description != null &&
                                !product.description.isEmpty()) {
                            try {
                                List<String> descriptions = gson.fromJson(product.description, List.class);
                                if (descriptions.get(0).toCharArray().length > 100) {
                                    String substring = descriptions.get(0).substring(0,100) + "...";
                                    productDescription.setText(substring);
                                }

                            } catch (Exception e) {
                                Log.d("Error", e.getLocalizedMessage());
                                if (product.description.toCharArray().length > 100) {
                                    String substring = product.description.substring(0,100) + "...";
                                    productDescription.setText(substring);
                                }
                            }

                        }

                        TextView price = view.findViewById(R.id.product_price_view);
                        price.setText(product.price);

                        List<String> urls = gson.fromJson(product.imageLink, List.class);
                        String url = urls.isEmpty() ? null : urls.get(0);

                        NetworkImageView image = view.findViewById(R.id.product_image);
                        ImageRequester imageRequester = ImageRequester.getInstance();
                        imageRequester.setImageFromUrl(image, url);

                        qtyText = view.findViewById(R.id.product_quantity_view);

                        MaterialButton button = view.findViewById(R.id.add_to_cart);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                OrderLine orderLine = new OrderLine();
                                orderLine.setProductId(product.productId);
                                orderLine.setProductName(product.productName);
                                orderLine.setPrice(product.price);
                                orderLine.setQuantity(qtyText.getText().toString());
                                OrderSingleton.getInstance()
                                        .setOrderLine(orderLine);
                                qtyText.setText(qtyText.getText().toString());
                                qtyText.setEnabled(false);

                                BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.orders_view);
                                badge.setVisible(true, true);
                                badge.setNumber(OrderSingleton.getInstance().getOrderLines().size());

                            }
                        });

                        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
                        String preferredSupermarketId = "641367ea3a921d2f1fb91e0f";


                        buildProductGrid(preferredSupermarketId, recyclerView);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(request);


        return view;
    }

    private void buildProductGrid(String supermarketId, RecyclerView recyclerView) {

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        String url = Constants.HOST + Constants.PRODUCT_URI + "?supermarketId=" + supermarketId;

        products = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        List<LinkedTreeMap> productMap = gson.fromJson(response, List.class);

                        for (LinkedTreeMap linkedTreeMap : productMap) {

                            String urlStrings = linkedTreeMap.get("imageLink").toString();
                            List<String> urls = gson.fromJson(urlStrings, List.class);
                            String url = urls.isEmpty() ? null : urls.get(0);

                            Product product = new Product(
                                    linkedTreeMap.get("productName").toString(),url,
                                    linkedTreeMap.get("price").toString(),
                                    null,
                                    linkedTreeMap.get("productId").toString()
                            );
                            products.add(product);
                        }

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
                        ProductCardRecyclerViewAdapter adapter = new ProductCardRecyclerViewAdapter(products, getActivity());
                        recyclerView.setAdapter(adapter);
                        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
                        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
                        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(request);
    }
}