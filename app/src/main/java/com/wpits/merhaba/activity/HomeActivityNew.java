package com.wpits.merhaba.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.google.android.material.navigation.NavigationView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.wpits.merhaba.R;
import com.wpits.merhaba.adapter.ViewPagerAdapter;
import com.wpits.merhaba.databinding.ActivityHomeNewBinding;
import com.wpits.merhaba.helper.JsonUtils;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.model.BottomTapLayout;
import com.wpits.merhaba.model.category.Category;
import com.wpits.merhaba.remoteConfig.RemoteConfigure;
import com.wpits.merhaba.utility.Utility;
import com.wpits.merhaba.utils.AppConstant;
import com.wpits.merhaba.utils.MySingleton;
import com.wpits.merhaba.utils.ViewPagerFragmentSelection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import phonenumberui.PhoneNumberActivity;

public class HomeActivityNew extends BaseActivity implements ViewPager.OnPageChangeListener, BottomTapLayout.TabListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    ActivityHomeNewBinding binding;
    private BottomTapLayout bottomTapLayout;
    private final int TOTAL_TABS = 3;
    private int INITIAL_POSITION = AppConstant.Tabs.HOME;
    private ViewPagerAdapter viewPagerAdapter;
    Context context;
    List<Category> allSampleData = new ArrayList<>();
    boolean isArabic = Utility.isArabic();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if( RemoteConfigure.getFirebaseRemoteConfig(context).getRemoteConfigValue(RemoteConfigure.crash_app).equalsIgnoreCase("true")){
             return;
        }
        context = this;
        setupBottomTabs();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), TOTAL_TABS);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.viewPager.addOnPageChangeListener(this);
        binding.viewPager.setOffscreenPageLimit(TOTAL_TABS);
        binding.viewPager.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(INITIAL_POSITION);
            }
        });
        binding.viewPager.setCurrentItem(1);
        binding.toolbar.tvHeaderDrawerIco.setOnClickListener(this);
        setUpNavigationDrawerMenu();
        getCategory();
        binding.toolbar.tvHeaderSearchIco.setOnClickListener(this);
        if(isArabic){
            binding.toolbar.tvHeaderTitle.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_app_name_ico_ar));

        }else{
            binding.toolbar.tvHeaderTitle.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_app_name_ico));

        }



    }

    private void setupBottomTabs() {
        List<BottomTapLayout.Tab> tabList = new ArrayList<>();


        tabList.add(new BottomTapLayout.Tab(AppConstant.Tabs.HOME, R.drawable.ic_outline_dashboard_24, R.drawable.ic_outline_dashboard_24,
                ContextCompat.getColor(context, R.color.colorAccent), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.title_category), context.getString(R.string.title_category)));

        tabList.add(new BottomTapLayout.Tab(AppConstant.Tabs.FAVOURITE, R.drawable.ic_add_fav, R.drawable.ic_add_fav,
                ContextCompat.getColor(context, R.color.colorAccent), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.title_favourite), context.getString(R.string.title_favourite)));
        tabList.add(new BottomTapLayout.Tab(AppConstant.Tabs.SETTINGS, R.drawable.ic_settings_ico, R.drawable.ic_settings_ico,
                ContextCompat.getColor(context, R.color.colorAccent), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.title_settings), context.getString(R.string.title_settings)));
//        tabList.add(new BottomTapLayout.Tab(AppConstant.Tabs.MORE, R.drawable.ic_more_ico, R.drawable.ic_more_ico,
//                ContextCompat.getColor(context, R.color.colorAccent), ContextCompat.getColor(context, R.color.theme_light_text),
//                context.getString(R.string.title_more), context.getString(R.string.title_more)));


        bottomTapLayout = new BottomTapLayout();
        bottomTapLayout.setup(context, binding.layoutBottomTabs, tabList, this);

        bottomTapLayout.setTab(INITIAL_POSITION);
    }

    @Override
    public void onPositionChange(int currentPosition, BottomTapLayout.Tab tab, List<BottomTapLayout.Tab> tabList) {
        binding.viewPager.setCurrentItem(tab.id);
    }

    @Override
    public void onReselection(int currentPosition, BottomTapLayout.Tab tab, List<BottomTapLayout.Tab> tabList) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        try {
            ((ViewPagerFragmentSelection) viewPagerAdapter.getFragments().get(position)).onTabSelection(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bottomTapLayout.setTab(position);


    }



    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void setUpNavigationDrawerMenu() {

//        ActionBarDrawerToggle drawerToggle=new ActionBarDrawerToggle(this,
//               binding.drawerLayout,
//                binding.toolbar.toolbar,
//                R.string.drawer_open,
//                R.string.drawer_close);
//
//        binding.drawerLayout.addDrawerListener(drawerToggle);
//        drawerToggle.syncState();

        binding.navigationView.setNavigationItemSelectedListener(HomeActivityNew.this);
        addMenuItemInNavMenuDrawer();
    }
    private void addMenuItemInNavMenuDrawer() {

        Menu menu = binding.navigationView.getMenu();
        menu.clear();
        Menu submenu = menu.addSubMenu(0,0,0,context.getResources().getString(R.string.title_category));

        if(allSampleData!=null&&allSampleData.size()>0){
            for (Category cat:allSampleData) {
                if(isArabic){
                    submenu.add(0,cat.getId(),cat.getId(),cat.getCategoryNameAr());

                }else{
                    submenu.add(0,cat.getId(),cat.getId(),cat.getCategoryName());

                }
            }
        }


        menu.add(1,1,1,context.getResources().getString(R.string.title_favourite));
        if(!PrefrenceManager.getInstance().isLoggedIn()){
            menu.add(2,2,2,context.getResources().getString(R.string.login));


        }else{
            menu.add(2,2,2,context.getResources().getString(R.string.logout));

        }




        binding.navigationView.invalidate();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        closeDrawer();
        switch (menuItem.getGroupId()){
            case 0:
                SongActivity.open(context,menuItem.getItemId());
               // PlayerActivity.open(context,0,menuItem.getItemId());
                break;
            case 1:
                bottomTapLayout.setTab(INITIAL_POSITION);
                binding.viewPager.setCurrentItem(AppConstant.Tabs.FAVOURITE);

                break;

            case 2:
                Intent i = new Intent(HomeActivityNew.this, PhoneNumberActivity.class);
                startActivity(i);
                finish();
                PrefrenceManager.getInstance().setIsLoggedIn(false);

                break;
        }
        return false;
    }

    private void openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.END);
    }

    private void closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.END);
        //start will make the drawer open from left
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_header_drawer_ico:
                openDrawer();
                break;
            case R.id.tv_header_search_ico:
                SearchActivity.openActivity(context,HomeActivityNew.this,v,0);
                break;

        }
    }

    private void getCategory() {
        String x="https://www.marhaba.com.ly:18086/crbt/v1/category/List";

        JsonArrayRequest categoryRequest=new JsonArrayRequest(Request.Method.GET, x, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Category category;
                Log.d("CategoryResponse",response.toString());
                try {
                    for(int i=0;i<response.length();i++){
                        if(i>7){
                            break;
                        }

                        JSONObject categoryObject=response.getJSONObject(i);
                        category=new Category();
                        category.setId(categoryObject.getInt("id"));
                        category.setCategoryName(categoryObject.getString("categoryName"));
                        category.setCategoryNameAr(categoryObject.getString("categoryNameAr"));
                        allSampleData.add(category);


                    }
                    PrefrenceManager.getInstance().saveCategories(response.toString());
                    addMenuItemInNavMenuDrawer();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CategoryErrorResponse",error.toString());

            }
        });
        categoryRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequest(categoryRequest);
    }

}