package com.example.service_ui;

import static com.example.service_ui.constants.Constants.MENU_ITEM_HOME;
import static com.example.service_ui.constants.Constants.MENU_ITEM_ORDERS;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class LandingActivity extends AppCompatActivity implements NavigationHost {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home_view) {
                    item.setChecked(true);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.container, new ViewProductsFragment())
                            .commit();
                } else if (item.getItemId() == R.id.orders_view) {
                    item.setChecked(true);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.container, new ViewOrderFragment())
                            .commit();
                }

                return false;
            }
        });

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

        transaction.addToBackStack(name);
        transaction.commit();
    }

}