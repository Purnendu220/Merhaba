package com.wpits.merhaba.model.favResponse;

import java.io.Serializable;

public class SubscriberIdDTO implements Serializable {
	private int id;
	private String msisdn;
	private String name;
	private String password;
	private String email;
	private String location;
	private String profilePicPath;
	private String isAppUser;
	private String deviceType;
	private String isActive;
	private String createdOn;
	private String updatedOn;
	private ChangeLanguageDTO changeLanguage;

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

	public void setChangeLanguage(ChangeLanguageDTO changeLanguage){
		this.changeLanguage = changeLanguage;
	}

	public ChangeLanguageDTO getChangeLanguage(){
		return changeLanguage;
	}

	@Override
 	public String toString(){
		return 
			"SubscriberIdDTO{" + 
			"id = '" + id + '\'' + 
			",msisdn = '" + msisdn + '\'' + 
			",name = '" + name + '\'' + 
			",password = '" + password + '\'' + 
			",email = '" + email + '\'' + 
			",location = '" + location + '\'' + 
			",profilePicPath = '" + profilePicPath + '\'' + 
			",isAppUser = '" + isAppUser + '\'' + 
			",deviceType = '" + deviceType + '\'' + 
			",isActive = '" + isActive + '\'' + 
			",createdOn = '" + createdOn + '\'' + 
			",updatedOn = '" + updatedOn + '\'' + 
			",changeLanguage = '" + changeLanguage + '\'' + 
			"}";
		}
}