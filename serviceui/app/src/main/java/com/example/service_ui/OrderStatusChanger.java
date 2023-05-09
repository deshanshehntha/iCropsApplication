package com.example.service_ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.service_ui.R;
import com.example.service_ui.constants.Constants;
import com.example.service_ui.model.OrderLine;
import com.google.android.material.badge.BadgeDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class OrderStatusChanger extends DialogFragment {
    private String orderLineId;

    OrderStatusChanger(String orderLineId) {
        this.orderLineId = orderLineId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change status")
                .setItems(R.array.order_status, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String[] statuses = getResources().getStringArray(R.array.order_status);
                        String selectedStatus = statuses[which];

                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        String url = Constants.HOST + Constants.ORDER_LINES_URI + "/" + orderLineId;

                        JSONObject object = new JSONObject();
                        try {
                            object.put("orderStatus", selectedStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        dialog.dismiss();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                        requestQueue.add(jsonObjectRequest);
                    }
                });
        return builder.create();
    }
}