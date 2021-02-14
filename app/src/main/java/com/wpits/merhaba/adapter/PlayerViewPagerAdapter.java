package com.wpits.merhaba.adapter;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.wpits.merhaba.activity.ui.player.PlayerFragment;
import com.wpits.merhaba.model.album.Song;

import java.util.HashMap;
import java.util.List;



public class PlayerViewPagerAdapter extends FragmentStatePagerAdapter {

    private int tabsCount;
    private List<Song> mSongsList;
    private PlayerFragment mCurrentFragment;
    private HashMap<Integer,Fragment> fragmentMap = new HashMap<>();

    public Fragment getCurrentItem(int position) {
        return fragmentMap.get(position);
    }

    public Song getCurrentSong(int position) {
        return mSongsList.get(position);
    }

    public PlayerViewPagerAdapter(FragmentManager fm,List<Song> songsList) {
        super(fm);
        this.tabsCount = songsList.size();
        mSongsList = songsList;

    }




    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        frag =  PlayerFragment.newInstance(mSongsList.get(position),position);
        fragmentMap.put(position,frag);
        Log.d("onPageSelected","getItem position "+position);
        return frag;
    }

    @Override
    public int getCount() {
        return tabsCount;
    }


}