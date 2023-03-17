package com.example.service_ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.service_ui.constants.UriConstants;
import com.example.service_ui.model.Supermarket;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SetupSupermarketFragment extends Fragment {

    private RequestQueue requestQueue;
    private List<Supermarket> supermarkets;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_supermarket, container, false);

        Bundle bundle = getArguments();
        String userId = bundle.getString("userId");

        requestQueue = Volley.newRequestQueue(this.getContext());

        getCustomer(view, userId);
        return view;
    }


    private void getCustomer(View view, String userId) {


        String url = UriConstants.HOST + UriConstants.USER_CUSTOMER_GET_PATCH_URI + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject user = null;
                        try {
                            user = response;

                            String preferredSupermarket = user.getString("preferredSupermarket");
                            String userId = user.getString("userId");
                            String userType = user.getString("userType");

                            if (preferredSupermarket.equals("null")) {
                                getSupermarkets(view, userId);
                            } else {

                                ProductFragment productFragment = new ProductFragment();

                                Bundle bundle = new Bundle();
                                bundle.putString("preferredSupermarket", preferredSupermarket);
                                bundle.putString("userId", userId);
                                bundle.putString("userType", userType);

                                productFragment.setArguments(bundle);

                                ((NavigationHost) getActivity())
                                        .navigateTo(productFragment, false);
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest);

    }


    private void getSupermarkets(View view, String customerId) {
        supermarkets = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this.getContext());
        String url = UriConstants.HOST + UriConstants.SUPERMARKET_URI;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        List<LinkedTreeMap> supermarketMap = gson.fromJson(response, List.class);

                        for (LinkedTreeMap linkedTreeMap : supermarketMap) {
                            Supermarket supermarket = new Supermarket();
                            if (!Objects.isNull(linkedTreeMap.get("supermarketId"))) {
                                supermarket.setSupermarketId(linkedTreeMap.get("supermarketId").toString());
                            }
                            if (!Objects.isNull(linkedTreeMap.get("supermarketName"))) {
                                supermarket.setSupermarketName(linkedTreeMap.get("supermarketName").toString());
                            }
                            if (!Objects.isNull(linkedTreeMap.get("managerId"))) {
                                supermarket.setManagerId(linkedTreeMap.get("managerId").toString());
                            }
                            if (!Objects.isNull(linkedTreeMap.get("location"))) {
                                supermarket.setLocation(linkedTreeMap.get("location").toString());
                            }
                            supermarkets.add(supermarket);
                        }

                        LinearLayout layout = view.findViewById(R.id.supermarket_layout);

                        for (Supermarket supermarket : supermarkets) {
                            MaterialCheckBox supermarketBox = new MaterialCheckBox(getContext());

                            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);

                            supermarketBox.setLayoutParams(params);
                            supermarketBox.setText(supermarket.getSupermarketName());
                            supermarketBox.setTag(supermarket.getSupermarketId());

                            supermarketBox.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d("selected supermarket", supermarketBox.getTag().toString());
                                    updateCustomerProfile(customerId, supermarketBox.getTag().toString());
                                }
                            });

                            layout.addView(supermarketBox);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(request);
    }

    private void updateCustomerProfile(String userId, String supermarketId) {

        String url = UriConstants.HOST + UriConstants.USER_CUSTOMER_GET_PATCH_URI + userId;

        JSONObject object = new JSONObject();
        try {
            object.put("preferredSupermarket", supermarketId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        ProductFragment productFragment = new ProductFragment();

                        Bundle bundle = new Bundle();
                        bundle.putString("preferredSupermarket", supermarketId);
                        bundle.putString("userId", userId);

                        productFragment.setArguments(bundle);

                        ((NavigationHost) getActivity())
                                .navigateTo(productFragment, false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}