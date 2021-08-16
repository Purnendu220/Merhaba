package com.wpits.merhaba.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.wpits.merhaba.activity.ui.favourite.FavouriteFragment;
import com.wpits.merhaba.activity.ui.home.HomeFragment;
import com.wpits.merhaba.activity.ui.more.MoreFragment;
import com.wpits.merhaba.activity.ui.settings.SettingsFragment;
import com.wpits.merhaba.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;




public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    private int tabsCount;



    public ViewPagerAdapter(FragmentManager fm, int tabsCount) {
        super(fm);
        this.tabsCount = tabsCount;

        fragments = new ArrayList<>(tabsCount);

        for (int index = 0; index < tabsCount; index++) {
            fragments.add(null);
        }
    }



    public List<Fragment> getFragments() {
        return fragments;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment frag = null;
        if (position == AppConstant.Tabs.SETTINGS) {
            frag = new SettingsFragment();
        } else if (position == AppConstant.Tabs.HOME) {
            frag = new HomeFragment();

        } else if (position == AppConstant.Tabs.FAVOURITE) {
            frag = new FavouriteFragment();
        } else if (position == AppConstant.Tabs.MORE) {
            frag = new MoreFragment();
        }
        fragments.set(position, frag);
        return frag;
    }

    @Override
    public int getCount() {
        return tabsCount;
    }


}