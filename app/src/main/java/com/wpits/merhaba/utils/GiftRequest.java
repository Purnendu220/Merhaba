
package com.wpits.merhaba.utils;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;


public class GiftRequest extends JSONObject {

    @SerializedName("giftee")
    private String mGiftee;
    @SerializedName("gifter")
    private String mGifter;
    @SerializedName("mode")
    private String mMode;
    @SerializedName("rbtID")
    private String mRbtID;
    @SerializedName("remdur")
    private int mRemdur;
    @SerializedName("reminder")
    private String mReminder;


    public GiftRequest(String mGiftee, String mGifter, String mMode, String mRbtID, int mRemdur, String mReminder) {
        this.mGiftee = mGiftee;
        this.mGifter = mGifter;
        this.mMode = mMode;
        this.mRbtID = mRbtID;
        this.mRemdur = mRemdur;
        this.mReminder = mReminder;
    }

    public String getGiftee() {
        return mGiftee;
    }

    public void setGiftee(String giftee) {
        mGiftee = giftee;
    }

    public String getGifter() {
        return mGifter;
    }

    public void setGifter(String gifter) {
        mGifter = gifter;
    }

    public String getMode() {
        return mMode;
    }

    public void setMode(String mode) {
        mMode = mode;
    }

    public String getRbtID() {
        return mRbtID;
    }

    public void setRbtID(String rbtID) {
        mRbtID = rbtID;
    }

    public int getRemdur() {
        return mRemdur;
    }

    public void setRemdur(int remdur) {
        mRemdur = remdur;
    }

    public String getReminder() {
        return mReminder;
    }

    public void setReminder(String reminder) {
        mReminder = reminder;
    }

}
