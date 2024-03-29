package com.wpits.merhaba.helper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.preference.PowerPreference;
import com.preference.Preference;
import com.wpits.merhaba.model.VerifyOTPModel;
import com.wpits.merhaba.model.category.Category;
import com.wpits.merhaba.utility.Utility;
import com.wpits.merhaba.utils.AppConstant;

import org.json.JSONObject;

import java.util.List;

public class PrefrenceManager {
    private static PrefrenceManager sInstance;
    private final Preference prefrence;
    private Gson gson;


    private PrefrenceManager() {
        gson=new Gson();
        prefrence = PowerPreference.getDefaultFile();
    }

    public static PrefrenceManager getInstance() {
        if (sInstance == null) {
            sInstance = new PrefrenceManager();
        }
        return sInstance;
    }

    public void setUserData(String data) {
        prefrence.setString(AppConstant.Prefrences.USER_DATA,data);
    }
    public VerifyOTPModel getUserData() {
        VerifyOTPModel model= null;
        try{
            model=   JsonUtils.fromJson(prefrence.getString(AppConstant.Prefrences.USER_DATA,null), VerifyOTPModel.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public boolean isFirstRun() {
        return prefrence.getBoolean(AppConstant.Prefrences.IS_FIRST_RUN,true);
    }

    public void setFirstRun(boolean b) {
        prefrence.putBoolean(AppConstant.Prefrences.IS_FIRST_RUN,b);
    }

    public boolean isShowAppIntro() {
        return prefrence.getBoolean(AppConstant.Prefrences.IS_SHOW_APP_INTRO,true);
    }

    public void setShowAppIntro(boolean b) {
        prefrence.putBoolean(AppConstant.Prefrences.IS_SHOW_APP_INTRO,b);
    }
    public boolean isFirebaseIdUpdated() {
        return prefrence.getBoolean(AppConstant.Prefrences.IS_FIREBASE_ID_UPDATED,false);
    }
    public void setFirebaseIdUpdated(boolean b) {
        prefrence.putBoolean(AppConstant.Prefrences.IS_FIREBASE_ID_UPDATED,b);
    }

    public void setUserMobile(String s) {
        prefrence.putString(AppConstant.Prefrences.USER_MOBILE,s);
    }
    public String getUserMobile() {
        return prefrence.getString(AppConstant.Prefrences.USER_MOBILE,"");
    }
    public void saveCategories(String s) {
        prefrence.putString(AppConstant.Prefrences.ALL_CATEGORIES,s);
    }

    public List<Category> getAllCategories() {
        return Utility.getSavedCategories(prefrence.getString(AppConstant.Prefrences.ALL_CATEGORIES,""));
    }

    public void saveBanners(String s) {
        prefrence.putString(AppConstant.Prefrences.ALL_BANNERS,s);
    }

    public JSONObject getAllBanners() {
        try{
            JSONObject json = new JSONObject(prefrence.getString(AppConstant.Prefrences.ALL_BANNERS,""));
            return json;
        }catch (Exception e){
          return null;
        }

    }

    public void setIsLoggedIn(boolean s) {
        prefrence.putBoolean(AppConstant.Prefrences.USER_IS_LOGGED_IN,s);
    }
    public boolean isLoggedIn() {
        return prefrence.getBoolean(AppConstant.Prefrences.USER_IS_LOGGED_IN,false);
    }
    public void setUserLanguage(String s) {
        prefrence.putString(AppConstant.Prefrences.USER_LANGUAGE,s);
    }
    public String getUserLanguage() {
        return prefrence.getString(AppConstant.Prefrences.USER_LANGUAGE,AppConstant.Language.ARABIC);
    }




    public void clearData(){
        prefrence.clear();
    }

}
