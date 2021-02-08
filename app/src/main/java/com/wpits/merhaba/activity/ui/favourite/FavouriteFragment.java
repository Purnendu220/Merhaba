package com.wpits.merhaba.activity.ui.favourite;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.wpits.merhaba.R;
import com.wpits.merhaba.activity.PlayerActivity;
import com.wpits.merhaba.adapter.AdapterCallbacks;
import com.wpits.merhaba.adapter.SongsListViewAllAdapter;
import com.wpits.merhaba.databinding.FragmentFavouriteBinding;
import com.wpits.merhaba.helper.JsonUtils;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.model.AddToFavRequest;
import com.wpits.merhaba.model.album.Song;
import com.wpits.merhaba.model.favResponse.DataDTO;
import com.wpits.merhaba.model.favResponse.FavResponseDTO;
import com.wpits.merhaba.utility.Utility;
import com.wpits.merhaba.utils.MySingleton;
import com.wpits.merhaba.utils.ViewPagerFragmentSelection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FavouriteFragment extends Fragment implements ViewPagerFragmentSelection, AdapterCallbacks<Object> {

    private FavouriteViewModel favouriteViewModel;
    private Context mContext;
    private FragmentFavouriteBinding binding;
    SongsListViewAllAdapter songsListAdapter;
    List<Song> favsongs = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        favouriteViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FavouriteViewModel.class);

        binding = FragmentFavouriteBinding.inflate(inflater,container,false);
        mContext = getActivity();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(PrefrenceManager.getInstance().isLoggedIn()){
            binding.recyclerViewFavourite.setVisibility(View.VISIBLE);
            binding.btnSendConfirmationCode.setVisibility(View.GONE);
            binding.txtNoData.setVisibility(View.GONE);
            getFavList();
        }else{
            binding.recyclerViewFavourite.setVisibility(View.GONE);
            binding.btnSendConfirmationCode.setVisibility(View.VISIBLE);
            binding.txtNoData.setVisibility(View.GONE);
        }
      initRecyclerView();
    }
    private void initRecyclerView(){
        songsListAdapter = new SongsListViewAllAdapter(mContext,0,false,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        binding.recyclerViewFavourite.setLayoutManager(mLayoutManager);
        binding.recyclerViewFavourite.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewFavourite.setAdapter(songsListAdapter);
    }


    private void getFavList(){
        String x="https://www.marhaba.com.ly:18086/crbt/v1/getFavorites/"+PrefrenceManager.getInstance().getUserMobile();
        JsonObjectRequest addToFavRequestRequest=new JsonObjectRequest(Request.Method.GET, x,null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Favourite Response",response.toString());
                try {
                    FavResponseDTO resp = JsonUtils.fromJson(response.toString(),FavResponseDTO.class);
                    Log.d("Fav",resp.getData().size()+" Records");
                    favsongs = new ArrayList<>();
                    songsListAdapter.clearAll();
                    for (DataDTO dto:resp.getData()
                         ) {
                        Song model = dto.getTopContentId();
                        model.setFavStatus(true);
                        favsongs.add(model);
                    }

                    songsListAdapter.addAllClass(favsongs);
                    songsListAdapter.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CategoryErrorResponse",error.toString());

            }

        }) {
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
        };

        MySingleton.getInstance(mContext).addToRequest(addToFavRequestRequest);
    }

    @Override
    public void onTabSelection(int position) {

    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()){
            case R.id.addToFav:
                final Song song = (Song) model;
                unfavRequest(song);
                break;
            case R.id.imgPlay:
                final Song data = (Song) model;

                PlayerActivity.open(mContext,0,data.getCategoryId(),data);

                break;

        }

    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

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
                    Toast.makeText(mContext,"Song unmarked as Favourite",Toast.LENGTH_LONG);
                     getFavList();


                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CategoryErrorResponse",error.toString());
                dialog.dismiss();


            }
        });

        MySingleton.getInstance(mContext).addToRequest(addToFavRequestRequest);
    }

}