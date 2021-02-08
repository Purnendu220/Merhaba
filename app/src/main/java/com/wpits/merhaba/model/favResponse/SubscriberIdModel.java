package com.wpits.merhaba.model.favResponse;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class SubscriberIdModel implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("msisdn")
	private String msisdn;

	@SerializedName("name")
	private String name;

	@SerializedName("password")
	private String password;

	@SerializedName("email")
	private String email;

	@SerializedName("location")
	private String location;

	@SerializedName("profilePicPath")
	private String profilePicPath;

	@SerializedName("isAppUser")
	private String isAppUser;

	@SerializedName("deviceType")
	private String deviceType;

	@SerializedName("isActive")
	private String isActive;

	@SerializedName("createdOn")
	private String createdOn;

	@SerializedName("updatedOn")
	private String updatedOn;

	@SerializedName("changeLanguage")
	private ChangeLanguageModel changeLanguage;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setMsisdn(String msisdn){
		this.msisdn = msisdn;
	}

	public String getMsisdn(){
		return msisdn;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getLocation(){
		return location;
	}

	public void setProfilePicPath(String profilePicPath){
		this.profilePicPath = profilePicPath;
	}

	public String getProfilePicPath(){
		return profilePicPath;
	}

	public void setIsAppUser(String isAppUser){
		this.isAppUser = isAppUser;
	}

	public String getIsAppUser(){
		return isAppUser;
	}

	public void setDeviceType(String deviceType){
		this.deviceType = deviceType;
	}

	public String getDeviceType(){
		return deviceType;
	}

	public void setIsActive(String isActive){
		this.isActive = isActive;
	}

	public String getIsActive(){
		return isActive;
	}

	public void setCreatedOn(String createdOn){
		this.createdOn = createdOn;
	}

	public String getCreatedOn(){
		return createdOn;
	}

	public void setUpdatedOn(String updatedOn){
		this.updatedOn = updatedOn;
	}

	public String getUpdatedOn(){
		return updatedOn;
	}

	public void setChangeLanguage(ChangeLanguageModel changeLanguage){
		this.changeLanguage = changeLanguage;
	}

	public ChangeLanguageModel getChangeLanguage(){
		return changeLanguage;
	}
}