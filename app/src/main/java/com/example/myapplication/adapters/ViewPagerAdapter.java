package com.example.myapplication.adapters;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }
    public ViewPagerAdapter(FragmentActivity fragment) {
        super(fragment);
    }
    @NonNull
    public Fragment createFragment(int position) {
        return mFragmentList.get(position);
    }
    public int getItemCount() {
        return mFragmentList.size();
    }
}
