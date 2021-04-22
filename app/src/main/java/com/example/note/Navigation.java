package com.example.note;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Navigation {
    private FragmentActivity activity;
    private final FragmentManager fragmentManager;

    public Navigation(FragmentActivity activity) {
        this.activity = activity;
        fragmentManager = this.activity.getSupportFragmentManager();

    }

    public void showFragment(Fragment fragment, boolean addToBackStack){
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_out_right)
                .replace(R.id.fragmentContainer, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
