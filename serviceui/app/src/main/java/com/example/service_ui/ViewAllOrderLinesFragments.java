package com.example.service_ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.service_ui.constants.Constants;
import com.example.service_ui.model.Order;
import com.example.service_ui.model.OrderLine;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ViewAllOrderLinesFragments extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_all_order_lines_fragment, container, false);

        BottomNavigationView bottomNavigationView = container.findViewById(R.id.sup_bottom_navigation);

        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String supplierId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, null);

        String url = Constants.HOST + Constants.ORDER_LINES_URI + "/" + supplierId;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());


        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Gson gson = new Gson();
                        List<LinkedTreeMap> orderLines = gson.fromJson(response, List.class);

                        List<OrderLine> orderLinesList = new ArrayList<>();

                        for (LinkedTreeMap mappedOrderLine: orderLines) {
                            OrderLine orderLine = new OrderLine();
                            orderLine.setOrderLineId(mappedOrderLine.get("orderLineId").toString());
                            orderLine.setProductId(mappedOrderLine.get("productId").toString());
                            orderLine.setProductName(mappedOrderLine.get("productName").toString());
                            orderLine.setPrice(mappedOrderLine.get("price").toString());
                            orderLine.setQuantity(mappedOrderLine.get("quantity").toString());
                            orderLinesList.add(orderLine);
                        }


                        RecyclerView recyclerView = view.findViewById(R.id.orderlines_recycler_view);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
                        OrderCardRecyclerViewAdapter adapter = new OrderCardRecyclerViewAdapter(orderLinesList, getActivity().getSupportFragmentManager(), false);
                        recyclerView.setAdapter(adapter);
                        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
                        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
                        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));

                        BottomNavigationView bottomNavigationView = container.findViewById(R.id.sup_bottom_navigation);
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