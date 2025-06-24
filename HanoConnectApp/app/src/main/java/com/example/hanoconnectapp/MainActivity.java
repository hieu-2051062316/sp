package com.example.hanoconnectapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hanoconnectapp.adapters.OrganizationViewPagerAdapter;
import com.example.hanoconnectapp.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        Intent intent = getIntent();
        String userRole = intent.getStringExtra("USER_ROLE");
        if (userRole == null) {
            userRole = "VOLUNTEER";
        }

        setupUIForRole(userRole);
    }

    private void setupUIForRole(String role) {
        FragmentStateAdapter adapter;
        if ("Organization".equalsIgnoreCase(role)) {
            adapter = new OrganizationViewPagerAdapter(this);
            viewPager.setAdapter(adapter);
            setupOrganizationTabs();
        } else {
            adapter = new ViewPagerAdapter(this);
            viewPager.setAdapter(adapter);
            setupVolunteerTabs();
        }
    }

    private void setupVolunteerTabs() {
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.ic_home_logo);
                    break;
                case 1:
                    tab.setIcon(R.drawable.ic_star);
                    break;
                case 2:
                    tab.setIcon(R.drawable.ic_person);
                    break;
            }
        }).attach();
    }

    private void setupOrganizationTabs() {
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.ic_home_logo);
                    break;
                case 1:
                    tab.setIcon(R.drawable.ic_campaign); // Icon cho mục Chiến dịch
                    break;
                case 2:
                    tab.setIcon(R.drawable.ic_person);
                    break;
            }
        }).attach();
    }
}
