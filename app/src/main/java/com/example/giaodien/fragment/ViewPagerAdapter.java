package com.example.giaodien.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Room_fragment();
            case 1:
                return new Customers_fragment();
            case 2:
                return new Bill_fragment();
            case 3:
                return new Account_fragment();
            default:
                return new Room_fragment();

        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
