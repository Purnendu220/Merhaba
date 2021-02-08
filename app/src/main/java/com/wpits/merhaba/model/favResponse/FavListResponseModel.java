package com.wpits.merhaba.model.favResponse;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class FavListResponseModel implements Serializable {

	@SerializedName("httpCode")
	private int httpCode;

	@SerializedName("status")
	private String status;

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private List<DataModel> data;

	@SerializedName("latencyTime")
	private String latencyTime;

	@SerializedName("timeStamp")
	private String timeStamp;

	public void setHttpCode(int httpCode){
		this.httpCode = httpCode;
	}

	public int getHttpCode(){
		return httpCode;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setData(List<DataModel> data){
		this.data = data;
	}

	public List<DataModel> getData(){
		return data;
	}

	public void setLatencyTime(String latencyTime){
		this.latencyTime = latencyTime;
	}

	public String getLatencyTime(){
		return latencyTime;
	}

	public void setTimeStamp(String timeStamp){
		this.timeStamp = timeStamp;
	}

	public String getTimeStamp(){
		return timeStamp;
	}
}