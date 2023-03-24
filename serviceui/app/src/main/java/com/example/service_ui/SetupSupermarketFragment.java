package com.example.service_ui;

import static com.example.service_ui.constants.Constants.SHARED_PREF_SUPERMARKET_ID;
import static com.example.service_ui.constants.Constants.SHARED_PREF_USER_ID;
import static com.example.service_ui.constants.Constants.SHARED_PREF_USER_TYPE;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.service_ui.constants.Constants;
import com.example.service_ui.model.Supermarket;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SetupSupermarketFragment extends Fragment {
    private RequestQueue requestQueue;
    private List<Supermarket> supermarkets;
    private SharedPreferences sharedpreferences;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_supermarket, container, false);

        sharedpreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String userId = sharedpreferences.getString(Constants.SHARED_PREF_USER_ID, null);

        requestQueue = Volley.newRequestQueue(this.getContext());

        getCustomer(view, userId);
        return view;
    }


    private void getCustomer(View view, String userId) {

        String url = Constants.HOST + Constants.USER_CUSTOMER_GET_PATCH_URI + userId;

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
                                getSupermarkets(view, userId, userType);
                            } else {

                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(SHARED_PREF_SUPERMARKET_ID, preferredSupermarket);
                                editor.putString(SHARED_PREF_USER_TYPE, userType);
                                editor.commit();

                                ((NavigationHost) getActivity())
                                        .navigateTo(new ViewProductsFragment(), "view_products");
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


    private void getSupermarkets(View view, String customerId, String userType) {
        supermarkets = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this.getContext());
        String url = Constants.HOST + Constants.SUPERMARKET_URI;

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
                                    updateCustomerProfile(customerId, supermarketBox.getTag().toString(), userType);
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

    private void updateCustomerProfile(String userId, String supermarketId, String userType) {

        String url = Constants.HOST + Constants.USER_CUSTOMER_GET_PATCH_URI + userId;

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

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(SHARED_PREF_SUPERMARKET_ID, supermarketId);
                        editor.putString(SHARED_PREF_USER_TYPE, userType);
                        editor.commit();

                        ((NavigationHost) getActivity())
                                .navigateTo(new ViewProductsFragment(),"view_products");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}