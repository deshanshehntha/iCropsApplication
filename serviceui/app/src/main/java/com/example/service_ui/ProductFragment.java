package com.example.service_ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.service_ui.constants.UriConstants;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProductFragment extends Fragment {
    private RequestQueue requestQueue;
    private List<ProductEntry> products;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        //Add supermarket products here
        String preferredSupermarketId = "641367ea3a921d2f1fb91e0f";

        View view = inflater.inflate(R.layout.fragment_product, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        setUpToolbar(view);

        buildProductGrid(preferredSupermarketId, recyclerView);

        return view;
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    private void buildProductGrid(String supermarketId, RecyclerView recyclerView) {

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        String url = UriConstants.HOST + UriConstants.PRODUCT_URI + "?supermarketId=" + supermarketId;

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

                            ProductEntry productEntry = new ProductEntry(
                                    linkedTreeMap.get("productName").toString(),url,
                                    linkedTreeMap.get("price").toString(),
                                    null,
                                    linkedTreeMap.get("productId").toString()
                            );
                            products.add(productEntry);
                        }

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
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