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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.service_ui.constants.Constants;
import com.example.service_ui.model.Order;
import com.example.service_ui.model.OrderLine;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

public class AllOrdersFragment extends Fragment {

    private List<Order> orders;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_orders, container, false);

        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, null);
        String userType = sharedPreferences.getString(Constants.SHARED_PREF_USER_TYPE, null);
        String supermarketId = sharedPreferences.getString(Constants.SHARED_PREF_SUPERMARKET_ID, null);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        String url = null;

        if ("CUSTOMER".equals(userType)) {
            url = Constants.HOST + Constants.ORDERS_URI + "/user/" + userId;
        } else if ("MANAGER".equals(userType)) {
            url = Constants.HOST + Constants.ORDERS_URI + "/supermarket/" + supermarketId;
        }

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        RecyclerView recyclerView = view.findViewById(R.id.all_orders_recycler_view);

                        Gson gson = new Gson();
                        List<LinkedTreeMap> orders = gson.fromJson(response, List.class);

                        List<Order> orderList = new ArrayList<>();

                        for (LinkedTreeMap linkedTreeMap : orders) {
                            Order order = new Order();
                            order.setOrderId(linkedTreeMap.get("orderId") == null ? "" :
                                    linkedTreeMap.get("orderId").toString());
                            order.setOrderStatus(linkedTreeMap.get("orderStatus") == null ? "" :
                                    linkedTreeMap.get("orderStatus").toString());
                            order.setOrderedDate(linkedTreeMap.get("orderedDate") == null ? "" :
                                    linkedTreeMap.get("orderedDate").toString());

                            List<OrderLine> orderLinesList = new ArrayList<>();

                            ArrayList<LinkedTreeMap> orderMap =
                                    (ArrayList<LinkedTreeMap>) linkedTreeMap.get("orderLines");
                            for (LinkedTreeMap mappedOrderLine: orderMap) {
                                OrderLine orderLine = new OrderLine();
                                orderLine.setOrderLineId(mappedOrderLine.get("orderLineId").toString());
                                orderLine.setProductId(mappedOrderLine.get("productId").toString());
                                orderLine.setProductName(mappedOrderLine.get("productName").toString());
                                orderLine.setPrice(mappedOrderLine.get("price").toString());
                                orderLine.setQuantity(mappedOrderLine.get("quantity").toString());
                                orderLine.setOrderLineStatus(mappedOrderLine.get("orderStatus").toString());
                                orderLinesList.add(orderLine);
                            }

                            order.setOrderLines(orderLinesList);

                            orderList.add(order);
                        }

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
                        AllOrderCardRecyclerViewAdapter adapter = new AllOrderCardRecyclerViewAdapter(orderList, getActivity());
                        recyclerView.setAdapter(adapter);
                        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
                        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
                        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));

                        BottomNavigationView bottomNavigationView = container.findViewById(R.id.mgr_bottom_navigation);
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