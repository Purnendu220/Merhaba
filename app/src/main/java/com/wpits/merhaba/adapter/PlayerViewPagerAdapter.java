package com.wpits.merhaba.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wpits.merhaba.activity.ui.favourite.FavouriteFragment;
import com.wpits.merhaba.activity.ui.home.HomeFragment;
import com.wpits.merhaba.activity.ui.more.MoreFragment;
import com.wpits.merhaba.activity.ui.player.PlayerFragment;
import com.wpits.merhaba.activity.ui.settings.SettingsFragment;
import com.wpits.merhaba.model.album.Song;
import com.wpits.merhaba.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;



public class PlayerViewPagerAdapter extends FragmentStatePagerAdapter {

    private int tabsCount;
    private List<Song> mSongsList;



    public PlayerViewPagerAdapter(FragmentManager fm,List<Song> songsList) {
        super(fm);
        this.tabsCount = songsList.size();
        mSongsList = songsList;

    }




    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        frag =  PlayerFragment.newInstance(mSongsList.get(position));
        return frag;
    }

    @Override
    public int getCount() {
        return tabsCount;
    }


}