package com.wpits.merhaba.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wpits.merhaba.R;
import com.wpits.merhaba.adapter.AdapterCallbacks;
import com.wpits.merhaba.adapter.SongsListViewAllAdapter;
import com.wpits.merhaba.databinding.ActivitySearchBinding;
import com.wpits.merhaba.model.album.Song;
import com.wpits.merhaba.utility.KeyboardUtils;
import com.wpits.merhaba.utility.LogUtils;
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

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, AdapterCallbacks<Object> {
    public static void openActivity(Context context, Activity activity, View sharedElement, int navigationFrom) {

        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElement, "searchIcon");

        context.startActivity(new Intent(context, SearchActivity.class)
                .putExtra(AppConstant.DataKey.NAVIGATED_FROM_INT, navigationFrom), activityOptionsCompat.toBundle());
    }
    ActivitySearchBinding binding;
    Context context;
    private Handler handlerUI;
    private Handler handlerBG;
    private int SEARCH_LIMIT = 2;
    private int SEARCH_DELAY = 400;

    private Runnable runnableSearch;
    private boolean isSearching = false;
    SongsListViewAllAdapter songsListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;
        binding.imageViewSearchCancel.setOnClickListener(this);
        binding.layoutSearch.setOnClickListener(this);
        binding.editTextSearch.setHint(context.getResources().getString(R.string.search));
        setupHandlers();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Transition sharedElementEnterTransition = getWindow().getSharedElementEnterTransition();
            sharedElementEnterTransition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    handlerUI.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.editTextSearch.setVisibility(View.VISIBLE);
                            binding.editTextSearch.requestFocus();
                            KeyboardUtils.open(binding.editTextSearch, context);

                        }
                    }, 100);
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        } else {
            binding.editTextSearch.setVisibility(View.VISIBLE);
            binding.editTextSearch.requestFocus();
            KeyboardUtils.open(binding.editTextSearch, context);
        }

        songsListAdapter=new SongsListViewAllAdapter(context,0,false,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerViewSearch.setLayoutManager(mLayoutManager);
        binding.recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewSearch.setAdapter(songsListAdapter);


        try {
            ((SimpleItemAnimator) binding.recyclerViewSearch.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        setupSearch();
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
    }

    private void setupSearch() {

        binding.editTextSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
//                    recyclerViewSearch.setVisibility(View.VISIBLE);
                    binding.layoutSearch.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.editTextSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                recyclerViewSearch.setVisibility(View.VISIBLE);
                binding.layoutSearch.setVisibility(View.VISIBLE);
                return false;
            }
        });

        binding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if (editable.toString().length() >= SEARCH_LIMIT) {

                    if (runnableSearch != null) {
                        handlerBG.removeCallbacks(runnableSearch);
                    }

                    if (MySingleton.getInstance(context).getRequestQue() != null) {
                        MySingleton.getInstance(context).getRequestQue().cancelAll("Search");

                    }

                    startSearching(editable.toString());

                } else if (editable.toString().length() == 0) {
                    if (runnableSearch != null) {
                        handlerBG.removeCallbacks(runnableSearch);
                    }

                    if (MySingleton.getInstance(context).getRequestQue() != null) {
                        MySingleton.getInstance(context).getRequestQue().cancelAll("Search");

                    }

                    clearSearch();

                } else {
                    if (runnableSearch != null) {
                        handlerBG.removeCallbacks(runnableSearch);
                    }

                    if (MySingleton.getInstance(context).getRequestQue() != null) {
                        MySingleton.getInstance(context).getRequestQue().cancelAll("Search");

                    }

                    clearSearch();
                }
            }
        });
    }

    private void startSearching(final String search) {
        runnableSearch = new Runnable() {

            @Override
            public void run() {
                handlerUI.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.imageViewImageSearch.setVisibility(View.GONE);
                        binding.progressBarDone.setVisibility(View.VISIBLE);
                    }
                });

                searchApi(search);
            }
        };

        isSearching = true;
        handlerBG.postDelayed(runnableSearch, SEARCH_DELAY);
    }


    private void clearSearch() {

        songsListAdapter.clearAll();

        handlerUI.post(new Runnable() {
            @Override
            public void run() {

                songsListAdapter.notifyDataSetChanged();
//                layoutSearch.setVisibility(View.GONE);
                binding.imageViewImageSearch.setVisibility(View.VISIBLE);
                binding.progressBarDone.setVisibility(View.GONE);
            }
        });
    }

    private void setupHandlers() {
        handlerUI = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                handlerBG = new Handler();
                Looper.loop();
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewImageSearch:
                break;

            case R.id.imageViewSearchCancel:

                if (binding.editTextSearch.getText().toString().isEmpty()) {
                    onBackPressed();
                } else
                    binding.editTextSearch.setText("");

                break;

            case R.id.layoutSearch:
                KeyboardUtils.close(binding.editTextSearch, context);

//                if (searchAdapter.isEmpty() && (searchPagerAdapter == null || searchAdapter.getSearchResult() == null)) {
                if (songsListAdapter.isEmpty() && songsListAdapter == null) {
                    binding.layoutSearch.setVisibility(View.GONE);
                    onBackPressed();
                } else if (songsListAdapter != null) {
                    binding.layoutSearch.setVisibility(View.GONE);
                }

                break;
        }
    }

    private List<Song> searchApi(String search) {

        final List<Song> songsList = new ArrayList<Song>();
        String album_url = "https://www.marhaba.com.ly:18083/topContent/topContentByCtgId?ctgId="+"1";
        JsonObjectRequest albumRequest=new JsonObjectRequest(Request.Method.GET, album_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                isSearching = false;

                songsListAdapter.clearAll();
                Log.d("Response --->",response.toString());

                int catCategoryId=1;

                try {
                    JSONArray data = response.getJSONArray("data");
                    Log.d("Songs ", response.toString());

                    for(int i=0;i<data.length();i++){

                        JSONObject topContent=data.getJSONObject(i);
                        //JSONObject categoryContent=categoryList.getJSONObject("album");
                        Log.d("Songs", topContent.toString());
                        JSONObject songContent = topContent.getJSONObject("topContent");
                        String favStatus = topContent.getString("favStatus");
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
                        song.setId(id);

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
                        song.setFavStatus(Utility.getFavStatus(favStatus));

                        Log.d("Songs", categoryId+" "+catCategoryId);

                        if(catCategoryId == categoryId) {
                            songsList.add(song);

                        }
                    }

                    songsListAdapter.addAllClass(songsList);
                    songsListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                binding.imageViewImageSearch.setVisibility(View.VISIBLE);
                binding.progressBarDone.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isSearching = false;
                binding.imageViewImageSearch.setVisibility(View.VISIBLE);
                binding.progressBarDone.setVisibility(View.GONE);
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
        albumRequest.addMarker("Search");
        MySingleton.getInstance(context).addToRequest(albumRequest);

        return songsList;
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }
}