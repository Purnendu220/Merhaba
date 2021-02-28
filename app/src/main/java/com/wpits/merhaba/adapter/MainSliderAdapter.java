package com.wpits.merhaba.adapter;

import com.wpits.merhaba.model.BannerModel;

import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {
    List<BannerModel> mList;

    public MainSliderAdapter(List<BannerModel> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        viewHolder.bindImageSlide(this.mList.get(position).getUrl());
    }
}