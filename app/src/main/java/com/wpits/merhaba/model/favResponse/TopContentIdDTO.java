package com.wpits.merhaba.model.favResponse;

import java.io.Serializable;

public class TopContentIdDTO implements Serializable {
	private int id;
	private int songId;
	private String songName;
	private String category;
	private int categoryId;
	private String order;
	private int uploaderType;
	private int isActive;
	private String contentPathLocation;
	private String albumArt;
	private String categoryNameAr;
	private String artistName;
	private String artistNameAr;
	private String songNameAr;
	private int userId;
	private int approverId;
	private int albumId;
	private String createdDate;
	private String approvedDate;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setSongId(int songId){
		this.songId = songId;
	}

	public int getSongId(){
		return songId;
	}

	public void setSongName(String songName){
		this.songName = songName;
	}

	public String getSongName(){
		return songName;
	}

	public void setCategory(String category){
		this.category = category;
	}

	public String getCategory(){
		return category;
	}

	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}

	public int getCategoryId(){
		return categoryId;
	}

	public void setOrder(String order){
		this.order = order;
	}

	public String getOrder(){
		return order;
	}

	public void setUploaderType(int uploaderType){
		this.uploaderType = uploaderType;
	}

	public int getUploaderType(){
		return uploaderType;
	}

	public void setIsActive(int isActive){
		this.isActive = isActive;
	}

	public int getIsActive(){
		return isActive;
	}

	public void setContentPathLocation(String contentPathLocation){
		this.contentPathLocation = contentPathLocation;
	}

	public String getContentPathLocation(){
		return contentPathLocation;
	}

	public void setAlbumArt(String albumArt){
		this.albumArt = albumArt;
	}

	public String getAlbumArt(){
		return albumArt;
	}

	public void setCategoryNameAr(String categoryNameAr){
		this.categoryNameAr = categoryNameAr;
	}

	public String getCategoryNameAr(){
		return categoryNameAr;
	}

	public void setArtistName(String artistName){
		this.artistName = artistName;
	}

	public String getArtistName(){
		return artistName;
	}

	public void setArtistNameAr(String artistNameAr){
		this.artistNameAr = artistNameAr;
	}

	public String getArtistNameAr(){
		return artistNameAr;
	}

	public void setSongNameAr(String songNameAr){
		this.songNameAr = songNameAr;
	}

	public String getSongNameAr(){
		return songNameAr;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setApproverId(int approverId){
		this.approverId = approverId;
	}

	public int getApproverId(){
		return approverId;
	}

	public void setAlbumId(int albumId){
		this.albumId = albumId;
	}

	public int getAlbumId(){
		return albumId;
	}

	public void setCreatedDate(String createdDate){
		this.createdDate = createdDate;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public void setApprovedDate(String approvedDate){
		this.approvedDate = approvedDate;
	}

	public String getApprovedDate(){
		return approvedDate;
	}

	@Override
 	public String toString(){
		return 
			"TopContentIdDTO{" + 
			"id = '" + id + '\'' + 
			",songId = '" + songId + '\'' + 
			",songName = '" + songName + '\'' + 
			",category = '" + category + '\'' + 
			",categoryId = '" + categoryId + '\'' + 
			",order = '" + order + '\'' + 
			",uploaderType = '" + uploaderType + '\'' + 
			",isActive = '" + isActive + '\'' + 
			",contentPathLocation = '" + contentPathLocation + '\'' + 
			",albumArt = '" + albumArt + '\'' + 
			",categoryNameAr = '" + categoryNameAr + '\'' + 
			",artistName = '" + artistName + '\'' + 
			",artistNameAr = '" + artistNameAr + '\'' + 
			",songNameAr = '" + songNameAr + '\'' + 
			",userId = '" + userId + '\'' + 
			",approverId = '" + approverId + '\'' + 
			",albumId = '" + albumId + '\'' + 
			",createdDate = '" + createdDate + '\'' + 
			",approvedDate = '" + approvedDate + '\'' + 
			"}";
		}
}