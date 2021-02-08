package com.wpits.merhaba.model.favResponse;

import java.util.List;
import java.io.Serializable;

public class FavResponseDTO implements Serializable {
	private int httpCode;
	private String status;
	private String message;
	private List<DataDTO> data;
	private String latencyTime;
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

	public void setData(List<DataDTO> data){
		this.data = data;
	}

	public List<DataDTO> getData(){
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

	@Override
 	public String toString(){
		return 
			"FavResponseDTO{" + 
			"httpCode = '" + httpCode + '\'' + 
			",status = '" + status + '\'' + 
			",message = '" + message + '\'' + 
			",data = '" + data + '\'' + 
			",latencyTime = '" + latencyTime + '\'' + 
			",timeStamp = '" + timeStamp + '\'' + 
			"}";
		}
}