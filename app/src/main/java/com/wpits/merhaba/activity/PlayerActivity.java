package com.wpits.merhaba.activity;

import android.content.Context;
import android.content.Intent;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wpits.merhaba.R;
import com.wpits.merhaba.activity.ui.player.PlayerFragment;
import com.wpits.merhaba.adapter.PlayerViewPagerAdapter;
import com.wpits.merhaba.databinding.ActivityPlayerBinding;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.model.album.Song;
import com.wpits.merhaba.utility.Utility;
import com.wpits.merhaba.utils.AppConstant;
import com.wpits.merhaba.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import phonenumberui.PhoneNumberActivity;

public class PlayerActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    ActivityPlayerBinding binding;
    Context context;
    boolean isArabic = Utility.isArabic();
    public PlayerViewPagerAdapter viewPagerAdapter;
    private List<Song> mListSongs = new ArrayList<>();
    private int INITIAL_POSITION = 0;
    public boolean isSuffleOn,isRepeatOn;
    public int navigatedFrom;
    private int categoryId=1;
    private Song currentSong ;
    public static void open(Context context, int navigationFrom,int categoryId) {
        context.startActivity(new Intent(context, PlayerActivity.class)
                .putExtra(AppConstant.DataKey.NAVIGATED_FROM_INT, navigationFrom)
                .putExtra(AppConstant.DataKey.CATEGORY_ID, categoryId));
    }
    public static void open(Context context, int navigationFrom,int categoryId,Song song) {
        context.startActivity(new Intent(context, PlayerActivity.class)
                .putExtra(AppConstant.DataKey.NAVIGATED_FROM_INT, navigationFrom)
                .putExtra(AppConstant.DataKey.SONG_DATA,song)
                .putExtra(AppConstant.DataKey.CATEGORY_ID, categoryId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;
        binding.toolbar.tvHeaderSearchIco.setVisibility(View.GONE);
        binding.toolbar.tvHeaderDrawerIco.setVisibility(View.GONE);

        navigatedFrom = getIntent().getIntExtra(AppConstant.DataKey.NAVIGATED_FROM_INT, -1);
        categoryId = getIntent().getIntExtra(AppConstant.DataKey.CATEGORY_ID,0);
        currentSong = (Song) getIntent().getSerializableExtra(AppConstant.DataKey.SONG_DATA);
        String catName = Utility.getCategoryName(PrefrenceManager.getInstance().getAllCategories(),categoryId,isArabic);
        if(isArabic){
            binding.toolbar.tvHeaderTitle.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_app_name_ico_ar));

        }else{
            binding.toolbar.tvHeaderTitle.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_app_name_ico));

        }
        if(catName!=null&&catName.length()>0){
        binding.toolbar.tvHeaderTitle.setVisibility(View.GONE);
        binding.toolbar.toolbar.setTitle(catName);

    }

        if(navigatedFrom==AppConstant.Navigated.FROM_FAVOURITES){
            getFavList();
        }else{
            albumApi(categoryId);
        }



    }

    private List<Song> albumApi(final int categoryId) {
        final List<Song> songsList = new ArrayList<Song>();
        String album_url = "https://www.marhaba.com.ly:18083/topContent/topContentByCtgId?ctgId="+categoryId;
        Log.d("URL",album_url);
        JsonObjectRequest albumRequest=new JsonObjectRequest(Request.Method.GET, album_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response --->",response.toString());

                int catCategoryId=categoryId;

                try {
                    JSONArray data = response.getJSONArray("data");
                    Log.d("Songs ", response.toString());

                    for(int i=0;i<data.length();i++){


                        JSONObject songContent=data.getJSONObject(i);
                        //JSONObject categoryContent=categoryList.getJSONObject("album");
                        Log.d("Songs", songContent.toString());
                       // JSONObject songContent = topContent.getJSONObject("topContent");
                        //String favStatus = topContent.getString("favStatus");                        //JSONObject categoryContent=categoryList.getJSONObject("album");


                        //JSONObject songContent = data.getJSONObject(i);


                        int id = songContent.getInt("id");
                        int songId =songContent.getInt("songId");
                        String songName =songContent.getString("songName");
                        int categoryId=songContent.getInt("categoryId");
                        String categoryName = songContent.getString("category");
                        String categoryNameAr=   songContent.getString("categoryNameAr");
                        String songsNameAr=   songContent.getString("songsNameAr");
                        String artistName=   songContent.getString("artistName");
                        String artistNameAr=   songContent.getString("artistNameAr");
                        String albumArt=   songContent.getString("albumArt");
                        String contentPathLocation=   songContent.getString("contentPathLocation");

                        Song song=new Song();
                        song.setAlbumArt(albumArt);
                        song.setArtistName(artistName);
                        song.setArtistNameAr(artistNameAr);
                        song.setCategoryId(categoryId);
                        song.setCategoryName(categoryName);
                        song.setCategoryNameAr(categoryNameAr);
                        song.setSongName(songName);
                        song.setSongsNameAr(songsNameAr);
                        song.setContentPathLocation(contentPathLocation);
                        song.setSongId(songId);
                        song.setId(id);
                        try{
                            String favStatus = songContent.getString("favStatus");
                            song.setFavStatus(Utility.getFavStatus(favStatus));

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Log.d("Songs", categoryId+" "+catCategoryId);

                        if(catCategoryId == categoryId) {
                            songsList.add(song);

                        }
                    }
                    mListSongs = songsList;
                    initViewPager();



                } catch (JSONException e) {
                    e.printStackTrace();
                }
      binding.loader2.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error-->",error.toString());
                binding.loader2.setVisibility(View.GONE);

            }

        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                return params;
            }
        }
                ;
        albumRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequest(albumRequest);

        return songsList;
    }


    private void getFavList(){
        String x="https://www.marhaba.com.ly:18086/crbt/v1/getFavorites/"+ PrefrenceManager.getInstance().getUserMobile();
        JsonArrayRequest addToFavRequestRequest=new JsonArrayRequest(Request.Method.GET, x,null , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("CategoryResponse",response.toString());
                try {




                } catch (Exception e) {
                    e.printStackTrace();
                }
                binding.loader2.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CategoryErrorResponse",error.toString());
                binding.loader2.setVisibility(View.GONE);

            }
        });
        addToFavRequestRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequest(addToFavRequestRequest);
    }


    private void initViewPager(){
        viewPagerAdapter = new PlayerViewPagerAdapter(getSupportFragmentManager(), mListSongs);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.viewPager.addOnPageChangeListener(this);
        binding.viewPager.setOffscreenPageLimit(0);
//        binding.viewPager.post(new Runnable() {
//            @Override
//            public void run() {
//                onPageSelected(INITIAL_POSITION);
//            }
//        });
        if(currentSong!=null){
            for (int i = 0; i < mListSongs.size(); i++) {
                if (mListSongs.get(i).getSongId().equals(currentSong.getSongId())) {
                    INITIAL_POSITION = i;

                    break;
                }
            }

        }

        binding.viewPager.setCurrentItem(INITIAL_POSITION);
        binding.viewPager.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                onPageSelected(INITIAL_POSITION);

            }
        }, 500);
    }

    public void openNextSong(){
        if(isSuffleOn){
            Random r = new Random();
            INITIAL_POSITION = r.nextInt(mListSongs.size()-1);
        }else{
            INITIAL_POSITION = binding.viewPager.getCurrentItem() +1;
            if(INITIAL_POSITION>mListSongs.size()-1){
                INITIAL_POSITION = 0;
            }
        }
        binding.viewPager.setCurrentItem(INITIAL_POSITION);

    }















    public boolean toggleSuffle(){
        isSuffleOn  = !isSuffleOn;
        return isSuffleOn;
    }
    public boolean toggleRepeat(){
        isRepeatOn  = !isRepeatOn;
        return isRepeatOn;
    }

    public void  playNext(){
        if(binding.viewPager.getCurrentItem()<viewPagerAdapter.getCount()-1){
            binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem()+1);

        }else{
            binding.viewPager.setCurrentItem(INITIAL_POSITION);

        }

    }
    public void  playPrev(){
        if(binding.viewPager.getCurrentItem()==INITIAL_POSITION){
            binding.viewPager.setCurrentItem(viewPagerAdapter.getCount()-1);

        }else{
            binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem()-1);

        }

    }
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        try{
            ((PlayerFragment)viewPagerAdapter.getCurrentItem(i)).play(viewPagerAdapter.getCurrentSong(i));

        }catch (Exception e){
            e.printStackTrace();

        }
       Log.d("onPageSelected","position:- "+i);

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
    public Song getCurrentSong(){
return viewPagerAdapter.getCurrentSong(binding.viewPager.getCurrentItem());

    }
}