package com.example.doan1.XuLyDonHang;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ChuaXacNhanDon_Fragment();
            case 1:
                return new DaXacNhanDon_Fragment();
            default:
                return new ChuaXacNhanDon_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}