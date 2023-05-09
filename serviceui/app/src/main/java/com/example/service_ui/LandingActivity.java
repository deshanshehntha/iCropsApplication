package com.example.service_ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.service_ui.constants.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class LandingActivity extends AppCompatActivity implements NavigationHost {
    private BottomNavigationView cus_bot_nav;
    private BottomNavigationView mgr_bot_nav;
    private BottomNavigationView sup_bot_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        SharedPreferences sharedPreferences =
                this.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String userType = sharedPreferences.getString(Constants.SHARED_PREF_USER_TYPE, null);

        cus_bot_nav = findViewById(R.id.cus_bottom_navigation);
        mgr_bot_nav = findViewById(R.id.mgr_bottom_navigation);
        sup_bot_nav = findViewById(R.id.sup_bottom_navigation);


        if ("CUSTOMER".equals(userType)) {
            mgr_bot_nav.setVisibility(View.GONE);
            sup_bot_nav.setVisibility(View.GONE);
            cus_bot_nav.setVisibility(View.VISIBLE);

            cus_bot_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.home_view) {
                        item.setChecked(true);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.container, new ViewProductsFragment())
                                .commit();
                    } else if (item.getItemId() == R.id.cart_view) {
                        item.setChecked(true);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.container, new ViewOrderFragment(null))
                                .commit();
                    } else if (item.getItemId() == R.id.orders_view) {
                        item.setChecked(true);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.container, new AllOrdersFragment())
                                .commit();
                    }

                    return false;
                }
            });

        } else if ("MANAGER".equals(userType)) {
            cus_bot_nav.setVisibility(View.GONE);
            mgr_bot_nav.setVisibility(View.VISIBLE);
            sup_bot_nav.setVisibility(View.GONE);

            mgr_bot_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.mgr_home) {
                        item.setChecked(true);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.container, new SummaryFragment())
                                .commit();
                    } else if (item.getItemId() == R.id.mgr_orders_view) {
                        item.setChecked(true);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.container, new AllOrdersFragment())
                                .commit();
                    }
                    return false;
                }
            });
        } else if ("SUPPLIER".equals(userType)) {
            cus_bot_nav.setVisibility(View.GONE);
            mgr_bot_nav.setVisibility(View.GONE);
            sup_bot_nav.setVisibility(View.VISIBLE);

            sup_bot_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.sup_home) {
                        item.setChecked(true);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.container, new SummaryFragment())
                                .commit();
                    } else if (item.getItemId() == R.id.sup_order_lines) {
                        item.setChecked(true);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.container, new ViewAllOrderLinesFragments())
                                .commit();
                    }
                    return false;
                }
            });
        }


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();
        }
    }

    @Override
    public void navigateTo(Fragment fragment, String name) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

//        transaction.addToBackStack(name);
        transaction.commit();
    }

}