package com.wpits.merhaba.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SongRatingResponse {

@SerializedName("httpCode")
@Expose
private Integer httpCode;
@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private List<Datum> data = null;
@SerializedName("latencyTime")
@Expose
private String latencyTime;
@SerializedName("timeStamp")
@Expose
private String timeStamp;

public Integer getHttpCode() {
return httpCode;
}

public void setHttpCode(Integer httpCode) {
this.httpCode = httpCode;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<Datum> getData() {
return data;
}

public void setData(List<Datum> data) {
this.data = data;
}

public String getLatencyTime() {
return latencyTime;
}

public void setLatencyTime(String latencyTime) {
this.latencyTime = latencyTime;
}

public String getTimeStamp() {
return timeStamp;
}

public void setTimeStamp(String timeStamp) {
this.timeStamp = timeStamp;
}

}