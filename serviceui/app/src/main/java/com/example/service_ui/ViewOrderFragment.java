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
import com.android.volley.toolbox.Volley;
import com.example.service_ui.constants.Constants;
import com.example.service_ui.model.Order;
import com.example.service_ui.model.OrderLine;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ViewOrderFragment extends Fragment {
    private List<OrderLine> orderLines;

    private Order order;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    ViewOrderFragment(Order order) {
        this.order = order;
        if (order == null) {
            orderLines = OrderSingleton.getInstance().getOrderLines();
        } else {
            orderLines = order.getOrderLines();
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String preferredSupermarketId = sharedPreferences.getString(Constants.SHARED_PREF_SUPERMARKET_ID, null);
        String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, null);
        String userType = sharedPreferences.getString(Constants.SHARED_PREF_USER_TYPE, null);


        View view = inflater.inflate(R.layout.fragment_view_cart, container, false);

        BottomNavigationView bottomNavigationView = container.findViewById(R.id.mgr_bottom_navigation);

        RecyclerView recyclerView = view.findViewById(R.id.orders_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        OrderCardRecyclerViewAdapter adapter = new OrderCardRecyclerViewAdapter(orderLines, getActivity().getSupportFragmentManager(), "CUSTOMER".equals(userType));
        recyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));

        Button button = view.findViewById(R.id.buy_button);

        if (order != null) {
            button.setEnabled(false);
            button.setText(order.getOrderStatus());
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                String url = Constants.HOST + Constants.ORDERS_URI;


                JSONArray jsonArray = new JSONArray();
                for (OrderLine orderLine: orderLines) {

                    JSONObject object = new JSONObject();
                    try {
                        object.put("productId", orderLine.getProductId());
                        object.put("quantity", orderLine.getQuantity());
                        object.put("price", orderLine.getPrice());
                        object.put("productName", orderLine.getProductName());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    jsonArray.put(object);
                }
                JSONObject object = new JSONObject();
                try {
                    object.put("supermarketId", preferredSupermarketId);
                    object.put("createUserId", userId);
                    object.put("orderLines", jsonArray);
                    object.put("orderType", "CUSTOMER_ORDER");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getContext(), "Order Created", Toast.LENGTH_SHORT).show();
                                button.setEnabled(false);

                                OrderSingleton.getInstance().clearOrderLines();

                                BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.cart_view);
                                badge.setVisible(false, false);
                                badge.clearNumber();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                requestQueue.add(jsonObjectRequest);
            }
        });


        return view;
    }


}