package com.wpits.merhaba.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wpits.merhaba.R;
import com.wpits.merhaba.utils.LanguageUtils;

import java.util.ArrayList;
import java.util.List;



public class BottomTapLayout implements View.OnClickListener {

    public static class Tab {
        public int imageResActive;
        public int imageResInactive;
        public int textColorActive;
        public int textColorInactive;
        public String textActive;
        public String textInactive;
        public boolean isActive;
        public int id = 0;

        public Tab(int id, int imageResActive, int imageResInactive, int textColorActive, int textColorInactive, String textActive, String textInactive) {
            this.id = id;
            this.imageResActive = imageResActive;
            this.imageResInactive = imageResInactive;
            this.textColorActive = textColorActive;
            this.textColorInactive = textColorInactive;
            this.textActive = textActive;
            this.textInactive = textInactive;
        }
    }

    private class TabView {
        public Tab tab;
        public LinearLayout linearLayout;
        public TextView textView;
        public ImageView imageView;
        public TextView textViewNotificationCounter;

        public TabView(Tab tab, LinearLayout linearLayout, TextView textView, ImageView imageView, TextView textViewNotificationCounter) {
            this.tab = tab;
            this.linearLayout = linearLayout;
            this.textView = textView;
            this.imageView = imageView;
            this.textViewNotificationCounter = textViewNotificationCounter;
        }
    }

    public interface TabListener {
        void onPositionChange(int currentPosition, Tab tab, List<Tab> tabList);

        void onReselection(int currentPosition, Tab tab, List<Tab> tabList);
    }

    private List<Tab> tabList;
    private List<TabView> tabViewList;
    private int currentlySelectedId = -1;
    private TabListener tabListener;
    private Context context;

    public void setup(Context context, ViewGroup viewGroup, List<Tab> tabList, TabListener tabListener) {
        this.tabList = tabList;
        this.context = context;
        this.tabListener = tabListener;
        tabViewList = new ArrayList<>(tabList.size());

        final int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
        int iconSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, context.getResources().getDisplayMetrics());
        int tabSize = (int) context.getResources().getDimension(R.dimen.bottom_tab_bar_height);

        LinearLayout linearLayoutMain = new LinearLayout(context);
        linearLayoutMain.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutMain.setBackgroundColor(Color.WHITE);
        linearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, tabSize));

//        TypedValue outValue = new TypedValue();
//        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);

        for (int count = 0; count < tabList.size(); count++) {

            Tab tab = tabList.get(count);

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setId(tab.id);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setClickable(true);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
//            linearLayout.setBackgroundResource(outValue.resourceId);

            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(iconSize, iconSize, Gravity.CENTER));
            imageView.setPadding(dp, dp * 4, dp, dp * 2);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            TextView textViewNotificationCounter = new TextView(context);
            textViewNotificationCounter.setLayoutParams(new FrameLayout.LayoutParams(13 * dp, 13 * dp, Gravity.RIGHT));
            textViewNotificationCounter.setBackgroundResource(R.drawable.notification_counter);
            textViewNotificationCounter.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
            textViewNotificationCounter.setTextColor(Color.WHITE);
            textViewNotificationCounter.setGravity(Gravity.CENTER);
            textViewNotificationCounter.setVisibility(View.INVISIBLE);
            textViewNotificationCounter.setText("");

            FrameLayout frameLayout = new FrameLayout(context);
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(iconSize + dp * 8, iconSize));

            frameLayout.addView(imageView);
            frameLayout.addView(textViewNotificationCounter);

            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(ContextCompat.getColor(context, R.color.theme_light_text));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
            textView.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));

            linearLayout.addView(frameLayout);
            linearLayout.addView(textView);

            linearLayoutMain.addView(linearLayout);

            tabViewList.add(new TabView(tab, linearLayout, textView, imageView, textViewNotificationCounter));
            linearLayout.setOnClickListener(this);
        }

        viewGroup.addView(linearLayoutMain);
    }

    public void setTab(int id) {
        for (TabView tabView : tabViewList) {
            if (tabView.tab.id == id) {
                tabView.linearLayout.performClick();
            }
        }
    }

    @Override
    public void onClick(View view) {

        for (Tab tab : tabList) {
            if (tab.id == view.getId()) {
                if (currentlySelectedId != view.getId()) {
                    currentlySelectedId = view.getId();
                    setTabView(currentlySelectedId);
                    tabListener.onPositionChange(currentlySelectedId, tab, tabList);
                } else {
                    tabListener.onReselection(currentlySelectedId, tab, tabList);
                }
            }
        }
    }

    public void getTabViewNotification(int id, int count) {
        for (int index = 0; index < tabViewList.size(); index++) {
            TabView tabView = tabViewList.get(index);

            if (tabView.tab.id == id) {
                tabView.textViewNotificationCounter.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
                tabView.textViewNotificationCounter.setText(LanguageUtils.numberConverter(count));
            }
        }
    }
 public void getTabViewMembership(int id,boolean isShowIcon) {
        for (int index = 0; index < tabViewList.size(); index++) {
            TabView tabView = tabViewList.get(index);

            if (tabView.tab.id == id) {
                tabView.textViewNotificationCounter.setVisibility(isShowIcon ? View.VISIBLE : View.GONE);
                tabView.textViewNotificationCounter.setText("");
            }
        }
    }

    private void setTabView(int id) {
        for (int index = 0; index < tabViewList.size(); index++) {
            TabView tabView = tabViewList.get(index);
            Tab tab = tabList.get(index);

            if (tabView.tab.id == id) {
                tabView.imageView.setImageResource(tab.imageResActive);
                tabView.imageView.setColorFilter(tab.textColorActive);
                tabView.textView.setText(tab.textActive);
                tabView.textView.setTextColor(tab.textColorActive);
                tab.isActive = true;
                tabView.linearLayout.animate().scaleXBy(.12f).scaleYBy(.12f).setDuration(100).setInterpolator(new LinearInterpolator()).start();

            } else {
                tabView.imageView.setImageResource(tab.imageResInactive);
                tabView.imageView.setColorFilter(tab.textColorInactive);
                tabView.textView.setText(tab.textInactive);
                tabView.textView.setTextColor(tab.textColorInactive);
                tab.isActive = false;
                tabView.linearLayout.animate().scaleX(1f).scaleY(1f).setDuration(200).setInterpolator(new LinearInterpolator()).start();
            }
        }
    }

    public int getCurrentlySelectedId() {
        return currentlySelectedId;
    }
}
