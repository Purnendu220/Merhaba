
package com.wpits.merhaba.model;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("language")
    private String mLanguage;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("subscription")
    private Subscription mSubscription;

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public Subscription getSubscription() {
        return mSubscription;
    }

    public void setSubscription(Subscription subscription) {
        mSubscription = subscription;
    }

}
