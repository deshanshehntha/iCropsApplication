package com.example.service_ui;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A product entry in the list of products.
 */
public class ProductEntry {
    private static final String TAG = ProductEntry.class.getSimpleName();
    public final String title;
    public final String url;
    public final String price;
    public final String description;

    public ProductEntry(
            String title, String url, String price, String description) {
        this.title = title;
        this.url = url;
        this.price = price;
        this.description = description;
    }
}