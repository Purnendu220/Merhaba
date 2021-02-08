
package com.wpits.merhaba.model;

import com.google.gson.annotations.SerializedName;


public class Subscription {

    @SerializedName("id")
    private String mId;
    @SerializedName("msisdn")
    private String mMsisdn;
    @SerializedName("songName")
    private String mSongName;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getMsisdn() {
        return mMsisdn;
    }

    public void setMsisdn(String msisdn) {
        mMsisdn = msisdn;
    }

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        mSongName = songName;
    }

}
