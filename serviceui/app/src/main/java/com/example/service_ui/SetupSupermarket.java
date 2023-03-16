package com.example.service_ui;

import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;


import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.service_ui.NavigationHost;
import com.example.service_ui.ProductCardRecyclerViewAdapter;
import com.example.service_ui.ProductEntry;
import com.example.service_ui.ProductFragment;
import com.example.service_ui.ProductGridItemDecoration;
import com.example.service_ui.R;
import com.example.service_ui.constants.UriConstants;
import com.example.service_ui.model.Supermarket;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SetupSupermarket extends Fragment {

    private RequestQueue requestQueue;
    private StringRequest request;

    private List<Supermarket> supermarkets;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_supermarket, container, false);
        getSupermarkets(view);
        return view;
    }

    private void getSupermarkets(View view) {
        supermarkets = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this.getContext());
        String url = UriConstants.HOST + UriConstants.SUPERMARKET_URI;

        request = new StringRequest(Request.Method.GET, url,
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
                            MaterialCheckBox supermarketBox =  new MaterialCheckBox(getContext());

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

}