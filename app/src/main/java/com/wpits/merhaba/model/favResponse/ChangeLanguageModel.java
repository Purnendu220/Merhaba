package com.wpits.merhaba.model.favResponse;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ChangeLanguageModel implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("langName")
	private String langName;

	@SerializedName("langCode")
	private String langCode;

	@SerializedName("countryName")
	private String countryName;

	@SerializedName("countryCode")
	private String countryCode;

	@SerializedName("createdDate")
	private String createdDate;

	@SerializedName("lastUpdated")
	private Object lastUpdated;

	@SerializedName("status")
	private String status;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setLangName(String langName){
		this.langName = langName;
	}

	public String getLangName(){
		return langName;
	}

	public void setLangCode(String langCode){
		this.langCode = langCode;
	}

	public String getLangCode(){
		return langCode;
	}

	public void setCountryName(String countryName){
		this.countryName = countryName;
	}

	public String getCountryName(){
		return countryName;
	}

	public void setCountryCode(String countryCode){
		this.countryCode = countryCode;
	}

	public String getCountryCode(){
		return countryCode;
	}

	public void setCreatedDate(String createdDate){
		this.createdDate = createdDate;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public void setLastUpdated(Object lastUpdated){
		this.lastUpdated = lastUpdated;
	}

	public Object getLastUpdated(){
		return lastUpdated;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}