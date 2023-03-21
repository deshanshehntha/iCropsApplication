package com.example.service_ui;

import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.service_ui.constants.UriConstants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private TextInputLayout passwordInput;
    private TextInputLayout usernameInput;
    private TextInputEditText passwordInputText;
    private TextInputEditText usernameInputText;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        passwordInput = view.findViewById(R.id.password_input_wrapper);
        passwordInputText = view.findViewById(R.id.password_input_text);

        usernameInput = view.findViewById(R.id.username_input_wrapper);
        usernameInputText = view.findViewById(R.id.username_input_text);

        MaterialButton loginButton = view.findViewById(R.id.next_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((NavigationHost) getActivity())
                        .navigateTo(new ProductFragment(), false);

                Editable username = usernameInputText.getText();
                Editable password = passwordInputText.getText();

//                if (isEmptyInputs(username, password)) {
//                    passwordInput.setError(getString(R.string.crops_empty_password));
//                    usernameInput.setError(getString(R.string.crops_empty_username));
//                } else {
//                    validateUserCredentials(username, password);
//                }
            }
        });

        passwordInputText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isNonEmptyInput(passwordInputText.getEditableText())) {
                    passwordInput.setError(null);
                }
                return false;
            }
        });

        usernameInputText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isNonEmptyInput(usernameInputText.getEditableText())) {
                    usernameInput.setError(null);
                }
                return false;
            }
        });

        return view;
    }

    private void validateUserCredentials(Editable username, Editable password) {

        String url = UriConstants.HOST + UriConstants.USER_VALIDATE_URI;

        requestQueue = Volley.newRequestQueue(this.getContext());

        JSONObject object = new JSONObject();
        try {
            object.put("userName", username.toString());
            object.put("password", password.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject user = null;
                        try {
                            user = response;
                            String requestStatus = user.getString("requestStatus");
                            if (requestStatus.equals("SUCCESS")) {
                                String userId = user.getString("userId");
                                String userType = user.getString("userType");
                                String name = user.getString("name");

                                if ("CUSTOMER".equals(userType)) {
                                    SetupSupermarketFragment setupSupermarketFragment = new SetupSupermarketFragment();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("userId", userId);
                                    setupSupermarketFragment.setArguments(bundle);

                                    ((NavigationHost) getActivity())
                                            .navigateTo(setupSupermarketFragment, false);
                                }
                            } else {
                                passwordInput.setError(getString(R.string.crops_invalid_credentials));
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

    private boolean isEmptyInputs(@Nullable Editable username, @Nullable Editable password) {
        return (username == null || username.length() == 0) && (password == null || password.length() == 0);
    }

    private boolean isNonEmptyInput(@Nullable Editable input) {
        return input.length() > 0;
    }


}