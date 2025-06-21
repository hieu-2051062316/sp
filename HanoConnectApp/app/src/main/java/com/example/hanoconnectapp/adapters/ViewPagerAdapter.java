package com.example.hanoconnectapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

// Tạm thời tạo các Fragment rỗng để code không báo lỗi
import com.example.hanoconnectapp.BlankFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Chúng ta sẽ thay thế các Fragment này bằng các Fragment thật sau
        switch (position) {
            case 1:
                return new BlankFragment(); // Followed Fragment
            case 2:
                return new BlankFragment(); // Notification Fragment
            case 3:
                return new BlankFragment(); // Profile Fragment
            case 0:
            default:
                return new BlankFragment(); // Home Fragment
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Số lượng tab
    }
}
