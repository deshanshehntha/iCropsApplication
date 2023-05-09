package com.example.service_ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.service_ui.constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;


public class ExplanationFragment extends Fragment {
    private String currentProdId;
    private String recProdId;
    private View layout_pending, layout_screen;
    private View view;

    public ExplanationFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentProdId = getArguments().getString("currentProdId");
            recProdId = getArguments().getString("recProdId");

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.explanation_fragment, container, false);

        layout_pending = view.findViewById(R.id.layout1);
        layout_screen = view.findViewById(R.id.layout2);
        layout_screen.setVisibility(View.GONE);
        layout_pending.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

        String url = Constants.HOST + Constants.PRODUCT_EXPLANATION_URI;

        JSONObject object = new JSONObject();
        try {
            object.put("initialAsin", currentProdId);
            object.put("recAsin", recProdId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject jsonResponse = null;
                        jsonResponse = response;

                        try {

                            TextView positiveText = view.findViewById(R.id.posText);
                            positiveText.setText(jsonResponse.getString("positiveSummary"));

                            TextView negText = view.findViewById(R.id.negText);
                            negText.setText(jsonResponse.getString("negativeSummary"));

                            TextView comparison = view.findViewById(R.id.compText);
                            comparison.setText(jsonResponse.getString("comparedText"));

                        } catch (Exception exception) {

                        }

                        layout_pending.setVisibility(View.GONE);
                        layout_screen.setVisibility(View.VISIBLE);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(1000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        return view;
    }
}