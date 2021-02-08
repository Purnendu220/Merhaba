
package com.wpits.merhaba.utils;

import com.google.gson.annotations.SerializedName;


public class VerifyOtpSuccessModel {

    @SerializedName("data")
    private Data mData;
    @SerializedName("httpCode")
    private Long mHttpCode;
    @SerializedName("latencyTime")
    private String mLatencyTime;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("timeStamp")
    private String mTimeStamp;

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public Long getHttpCode() {
        return mHttpCode;
    }

    public void setHttpCode(Long httpCode) {
        mHttpCode = httpCode;
    }

    public String getLatencyTime() {
        return mLatencyTime;
    }

    public void setLatencyTime(String latencyTime) {
        mLatencyTime = latencyTime;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        mTimeStamp = timeStamp;
    }

}
