package com.wpits.merhaba.activity.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.wpits.merhaba.R;
import com.wpits.merhaba.activity.PlayerActivity;
import com.wpits.merhaba.adapter.AdapterCallbacks;
import com.wpits.merhaba.adapter.HomeAdapter;
import com.wpits.merhaba.adapter.MainSliderAdapter;
import com.wpits.merhaba.helper.JsonUtils;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.model.AddToFavRequest;
import com.wpits.merhaba.model.BannerData;
import com.wpits.merhaba.model.CategoryListModel;
import com.wpits.merhaba.model.album.Song;
import com.wpits.merhaba.model.category.Category;
import com.wpits.merhaba.remoteConfig.RemoteConfigure;
import com.wpits.merhaba.utility.Utility;
import com.wpits.merhaba.utils.MySingleton;
import com.wpits.merhaba.utils.ViewPagerFragmentSelection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import phonenumberui.PhoneNumberActivity;
import pl.droidsonroids.gif.GifImageView;
import ss.com.bannerslider.Slider;


public class HomeFragment extends Fragment implements ViewPagerFragmentSelection, AdapterCallbacks<Object> {

    private HomeViewModel homeViewModel;
    boolean isArabic = Utility.isArabic();
    private Toolbar mToolbar;
    RecyclerView recyclerViewMain;
    List<Category> allSampleData = new ArrayList<>();
    Context mContext;
    HomeAdapter homeAdapter;
    GifImageView loader2;
    JcPlayerView jcplayerView;
    BannerData bannerData;
    Slider bannerSlider;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mToolbar =root.findViewById(R.id.toolbar);

        recyclerViewMain = root.findViewById(R.id.recyclerViewMain);
        loader2 = root.findViewById(R.id.loader2);
        jcplayerView = root.findViewById(R.id.jcplayer);
        bannerSlider = root.findViewById(R.id.banner_slider1);
        return root;
    }
    private void initRecyclerView(){

        homeAdapter = new HomeAdapter(mContext,0,false,this);
        recyclerViewMain.setHasFixedSize(false);
        recyclerViewMain.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewMain.setAdapter(homeAdapter);
    }
    private void getCategory() {
        String x="https://www.marhaba.com.ly:18086/crbt/v1/category/List";

        JsonArrayRequest categoryRequest=new JsonArrayRequest(Request.Method.GET, x, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Category category;
                Log.d("CategoryResponse",response.toString());
                try {
                    homeAdapter.clearAll();
                    for(int i=0;i<response.length();i++){
                        if(i>7){
                            break;
                        }

                        JSONObject categoryObject=response.getJSONObject(i);
                        category=new Category();
                        category.setId(categoryObject.getInt("id"));
                        category.setCategoryName(categoryObject.getString("categoryName"));
                        category.setCategoryNameAr(categoryObject.getString("categoryNameAr"));
                        Log.d("CategoryResponse2", String.valueOf(categoryObject.getInt("id")));
                        Log.d("CategoryResponse -->", category.getCategoryName()+" "+category.getId());
                        if(i<2){

                            homeAdapter.addClass(category);
                        }else{
                            allSampleData.add(category);

                        }



                    }
                    homeAdapter.addCategoryListModel(new CategoryListModel(allSampleData));

                    homeAdapter.notifyDataSetChanged();
                    loader2.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CategoryErrorResponse",error.toString());
                loader2.setVisibility(View.GONE);

            }
        });

        MySingleton.getInstance(mContext).addToRequest(categoryRequest);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext=getActivity();
        initRecyclerView();
        getCategory();
       String bannerList =  RemoteConfigure.getFirebaseRemoteConfig(mContext).getRemoteConfigValue(RemoteConfigure.bannerJson);
       bannerData =JsonUtils.fromJson(bannerList, BannerData.class);
        bannerSlider.setAdapter(new MainSliderAdapter(bannerData.getData()));




    }

    @Override
    public void onTabSelection(int position) {

    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()){
            case R.id.imgPlay:
                final Song data = (Song) model;
              //  play(data);
                PlayerActivity.open(mContext,0,data.getCategoryId(),data);
                break;

            case R.id.addToFav:
                final Song song = (Song) model;
                if(PrefrenceManager.getInstance().isLoggedIn()){
                    if(song.getFavStatus()){
                      unfavRequest(song);
                    }else{
                        addToFavourite(song);

                    }

                }else{
                    Intent i = new Intent(mContext, PhoneNumberActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }
                break;
        }
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }

    private void addToFavourite(Song mSong){
        final ProgressDialog dialog = Utility.showProgress(mContext);

        String x="https://www.marhaba.com.ly:18086/crbt/v1/favorites";
      // String jsonString = {"subscriber_id" : "Ronaldo", "top_content_id" : "soccer"};
      //  JSONObject jsonObject = new JSONObject("{\"subscriber_id\":\"+PrefrenceManager.getInstance().getUserMobile()+\",\"top_content_id\":\"++\"}");

        AddToFavRequest request =  new AddToFavRequest(PrefrenceManager.getInstance().getUserMobile(),mSong.getId()+"");
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("subscriber_id", PrefrenceManager.getInstance().getUserMobile());
            jsonRequest.put("top_content_id", mSong.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Fav Request", JsonUtils.toJson(jsonRequest));


        JsonObjectRequest addToFavRequestRequest=new JsonObjectRequest(Request.Method.POST, x,jsonRequest , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CategoryResponse",response.toString());
                try {
                    Toast.makeText(mContext,"Song marked as Favourite",Toast.LENGTH_LONG);
                    getCategory();
                    dialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CategoryErrorResponse",error.toString());
                dialog.dismiss();
                Toast.makeText(mContext,"Song marked as Favourite",Toast.LENGTH_LONG);

            }
        });

        MySingleton.getInstance(mContext).addToRequest(addToFavRequestRequest);
    }
    private void unfavRequest(Song mSong){
        final ProgressDialog dialog = Utility.showProgress(mContext);

        String x="https://www.marhaba.com.ly:18086/crbt/v1/deleteFavorite";
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("subscriber_id", PrefrenceManager.getInstance().getUserMobile());
            jsonRequest.put("top_content_id", mSong.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Fav Request", JsonUtils.toJson(jsonRequest));


        JsonObjectRequest addToFavRequestRequest=new JsonObjectRequest(Request.Method.POST, x,jsonRequest , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CategoryResponse",response.toString());
                try {
                    Toast.makeText(mContext,"Song marked as Favourite",Toast.LENGTH_LONG);
                    getCategory();
                    dialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.d("CategoryErrorResponse",error.toString());
                Toast.makeText(mContext,"Song marked as Favourite",Toast.LENGTH_LONG);

            }
        });

        MySingleton.getInstance(mContext).addToRequest(addToFavRequestRequest);
    }






}