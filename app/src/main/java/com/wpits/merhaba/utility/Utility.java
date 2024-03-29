// Copyright (C) 2018 INTUZ.

// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
// the following conditions:

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
// ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
// THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.wpits.merhaba.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.wpits.merhaba.adapter.MainSliderAdapter;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.model.BannerModel;
import com.wpits.merhaba.model.category.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Utility {


    public static boolean isArabic(){
        return PrefrenceManager.getInstance().getUserLanguage().equalsIgnoreCase("ar")?true:false;
    }
    public static void hideKeyBoardFromView(Activity mActivity) {
        InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = mActivity.getCurrentFocus();
        if (view == null) {
            view = new View(mActivity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(Activity mActivity) {
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    public static ProgressDialog showProgress(Context mContext) {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        return progressDialog;
    }

    public static void dialogShow(Activity mActivity, final ProgressDialog mpDialog) {
        if (mpDialog != null && !mpDialog.isShowing()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mpDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void dialogDismiss(Activity mActivity, final ProgressDialog mpDialog) {
        if (mpDialog != null && mpDialog.isShowing()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mpDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    public static void log(String message) {
        if (message != null) {
            Log.e("Phone Auth ", message);
        } else {
            Log.e("Message", "null");
        }
    }


    public static void showToast(final Context ctx, final String message) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static boolean getFavStatus(String status){
        if(status==null||status.equalsIgnoreCase("null")||status.equalsIgnoreCase("Not Favorite")){
            return false;
        }else{
            return true;
        }
    }
    public static String timeinMinute(long milliseconds){

        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;

        System.out.format("%d Milliseconds = %d minutes and %d seconds.", milliseconds, minutes, seconds);
        String min = minutes<10?"0"+minutes:minutes+"";
        String second = seconds<10?"0"+seconds:seconds+"";

        return min+":"+second;
    }

    public static List<Category> getSavedCategories(String list){
        Category category;
     List<Category> allSampleData = new ArrayList<>();
        try {
            JSONArray response = new JSONArray(list);
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


        } catch (JSONException e) {
            e.printStackTrace();
        }

    return allSampleData;
    }
    public static List<BannerModel> getBannerList(JSONObject response){
        if(response!=null){
            BannerModel bannerModel;
            List<BannerModel> bannerList = new ArrayList<>();

            Log.d("BannerResponse",response.toString());
            try {
                JSONArray data = response.getJSONArray("data");

                for(int i=0;i<data.length();i++){
                    JSONObject bannerContent=data.getJSONObject(i);
                    bannerModel=new BannerModel(bannerContent.getInt("id"),bannerContent.getString("filePath"));
                    bannerList.add(bannerModel);
                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
            return bannerList;
        }
        else{
           return new ArrayList<BannerModel>();
        }
    }
    public static String getCategoryName(List<Category> list,int categoryId,boolean isArabic) {
        String categoryName = "";
        for (Category cat:list
             ) {
            if(cat.getId()==categoryId){
                if(isArabic){
                    categoryName = cat.getCategoryNameAr();

                }else{
                    categoryName = cat.getCategoryName();

                }
            }

        }

       return categoryName;
    }
}
