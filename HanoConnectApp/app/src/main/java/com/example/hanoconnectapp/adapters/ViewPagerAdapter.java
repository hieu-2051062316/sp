package com.example.hanoconnectapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hanoconnectapp.HomeFragment;
import com.example.hanoconnectapp.fragments.FollowedFragment;
import com.example.hanoconnectapp.fragments.NotificationFragment;
import com.example.hanoconnectapp.fragments.ProfileFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new FollowedFragment();
            case 2:
                return new NotificationFragment();
            case 3:
                return new ProfileFragment();
            case 0:
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Số lượng tab
    }
}
