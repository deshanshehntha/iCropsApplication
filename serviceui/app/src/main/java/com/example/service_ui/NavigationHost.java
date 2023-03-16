package com.example.service_ui;

import androidx.fragment.app.Fragment;

public interface NavigationHost {

    void navigateTo(Fragment fragment, boolean addToBackstack);

}
