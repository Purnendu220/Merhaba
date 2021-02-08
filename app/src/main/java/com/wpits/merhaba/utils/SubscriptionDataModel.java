
package com.wpits.merhaba.utils;

import com.google.gson.annotations.SerializedName;


public class SubscriptionDataModel {

    @SerializedName("resultCode")
    private Long mResultCode;
    @SerializedName("resultDescription")
    private String mResultDescription;
    @SerializedName("serviceName")
    private String mServiceName;

    public Long getResultCode() {
        return mResultCode;
    }

    public void setResultCode(Long resultCode) {
        mResultCode = resultCode;
    }

    public String getResultDescription() {
        return mResultDescription;
    }

    public void setResultDescription(String resultDescription) {
        mResultDescription = resultDescription;
    }

    public String getServiceName() {
        return mServiceName;
    }

    public void setServiceName(String serviceName) {
        mServiceName = serviceName;
    }

}
