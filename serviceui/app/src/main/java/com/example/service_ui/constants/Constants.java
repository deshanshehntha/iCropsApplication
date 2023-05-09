package com.example.service_ui.constants;

import static android.provider.Settings.System.getString;

import com.example.service_ui.R;

public class Constants {
    public static final String HOST = "http://10.0.2.2:8080/";
    public static final String USER_VALIDATE_URI = "api/v1/user/validate";
    public static final String USER_CUSTOMER_GET_PATCH_URI = "api/v1/user/customer/";
    public static final String SUPERMARKET_URI = "api/v1/supermarket";
    public static final String PRODUCT_URI = "api/v1/products";
    public static final String ORDERS_URI = "api/v1/orders";
    public static final String ORDER_LINES_URI = "api/v1/orderLines";

    public static final String SHARED_PREF_NAME = "user_data";
    public static final String SHARED_PREF_USER_ID = "user_id";
    public static final String SHARED_PREF_SUPERMARKET_ID = "supermarket_id";
    public static final String SHARED_PREF_USER_TYPE = "user_type";
    public static final String MENU_ITEM_HOME = "Home";
    public static final String MENU_ITEM_ORDERS = "Orders";
    public static final String PRODUCT_EXPLANATION_URI = PRODUCT_URI + "/exp";


}
