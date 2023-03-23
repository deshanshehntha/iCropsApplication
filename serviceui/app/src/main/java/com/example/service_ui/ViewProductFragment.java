package com.example.service_ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.service_ui.constants.UriConstants;
import com.example.service_ui.model.OrderLine;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.util.List;

public class ViewProductFragment extends Fragment {
    private String productId;

    public ViewProductFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getString("productId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_product, container, false);

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        String url = UriConstants.HOST + UriConstants.PRODUCT_URI + "/" + productId;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ProductEntry productEntry = gson.fromJson(response, ProductEntry.class);
                        TextView productTitle = view.findViewById(R.id.product_title_view);
                        productTitle.setText(productEntry.productName);

                        TextView productDescription = view.findViewById(R.id.product_description_view);

                        if (productEntry.description != null &&
                                !productEntry.description.isEmpty()) {
                            try {
                                List<String> descriptions = gson.fromJson(productEntry.description, List.class);
                                if (descriptions.get(0).toCharArray().length > 100) {
                                    String substring = descriptions.get(0).substring(0,100) + "...";
                                    productDescription.setText(substring);
                                }

                            } catch (Exception e) {
                                Log.d("Error", e.getLocalizedMessage());
                                if (productEntry.description.toCharArray().length > 100) {
                                    String substring = productEntry.description.substring(0,100) + "...";
                                    productDescription.setText(substring);
                                }
                            }

                        }

                        TextView price = view.findViewById(R.id.product_price_view);
                        price.setText(productEntry.price);

                        List<String> urls = gson.fromJson(productEntry.imageLink, List.class);
                        String url = urls.isEmpty() ? null : urls.get(0);

                        NetworkImageView image = view.findViewById(R.id.product_image);
                        ImageRequester imageRequester = ImageRequester.getInstance();
                        imageRequester.setImageFromUrl(image, url);

                        EditText qtyText = view.findViewById(R.id.product_quantity_view);


                        MaterialButton button = view.findViewById(R.id.add_to_cart);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                OrderLine orderLine = new OrderLine();
                                orderLine.setProductId(productEntry.productId);
                                orderLine.setQuantity(qtyText.getText().toString());
                                OrderedProductHolder.getInstance()
                                        .setOrderLine(orderLine);
                            }
                        });

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