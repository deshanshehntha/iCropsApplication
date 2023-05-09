package com.example.service_ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.service_ui.constants.Constants;
import com.example.service_ui.model.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;


public class ViewProductsFragment extends Fragment {
    private RequestQueue requestQueue;
    private List<Product> products;
    private SharedPreferences sharedpreferences;
    private View view;
    private View layout_pending, layout_screen;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_products, container, false);

        layout_pending = view.findViewById(R.id.layout1);
        layout_screen = view.findViewById(R.id.layout2);
        layout_screen.setVisibility(View.GONE);
        layout_pending.setVisibility(View.VISIBLE);

        sharedpreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String preferredSupermarketId = sharedpreferences.getString(Constants.SHARED_PREF_SUPERMARKET_ID, null);

        BottomNavigationView bottomNavigationView = container.findViewById(R.id.mgr_bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        String url = Constants.HOST + Constants.PRODUCT_URI + "?supermarketId=" + preferredSupermarketId;

        products = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        layout_pending.setVisibility(View.GONE);
                        layout_screen.setVisibility(View.VISIBLE);


                        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

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
                                    linkedTreeMap.get("productId").toString(),
                                    null);
                            products.add(product);
                        }

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
                        ProductCardRecyclerViewAdapter adapter = new ProductCardRecyclerViewAdapter(products, getActivity(), false, null);
                        recyclerView.setAdapter(adapter);
                        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
                        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
                        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));

                        BottomNavigationView bottomNavigationView =
                                container.findViewById(R.id.cus_bottom_navigation);
                        bottomNavigationView.setVisibility(View.VISIBLE);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(request);

        return view;

    }

}