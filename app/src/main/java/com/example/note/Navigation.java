package com.example.note;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Navigation {
    private FragmentActivity activity;
    private FragmentManager fragmentManager;
    private final FragmentTransaction transaction;

    public Navigation(FragmentActivity activity) {
        this.activity = activity;
        fragmentManager = this.activity.getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

    }

    public Navigation setCustomAnimation(boolean customAnimation) {
        transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_out_right);
        return this;
    }

    public void showFragment(Fragment fragment, boolean addToBackStack) {
        transaction
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_out_right)
                .replace(R.id.fragmentContainer, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
