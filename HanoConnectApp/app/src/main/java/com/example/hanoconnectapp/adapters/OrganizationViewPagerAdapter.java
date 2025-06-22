package com.example.hanoconnectapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hanoconnectapp.fragments.CampaignsFragment;
import com.example.hanoconnectapp.fragments.DashboardFragment;
import com.example.hanoconnectapp.fragments.NotificationFragment;
import com.example.hanoconnectapp.fragments.OrgProfileFragment;

public class OrganizationViewPagerAdapter extends FragmentStateAdapter {

    public OrganizationViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new CampaignsFragment();
            case 2:
                return new NotificationFragment(); // Dùng lại NotificationFragment
            case 3:
                return new OrgProfileFragment();
            case 0:
            default:
                return new DashboardFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Organization cũng có 4 tab
    }
}
