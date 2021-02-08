package com.wpits.merhaba.model.favResponse;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class TopContentIdModel implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("songId")
	private int songId;

	@SerializedName("songName")
	private String songName;

	@SerializedName("category")
	private String category;

	@SerializedName("categoryId")
	private int categoryId;

	@SerializedName("order")
	private String order;

	@SerializedName("uploaderType")
	private int uploaderType;

	@SerializedName("isActive")
	private int isActive;

	@SerializedName("contentPathLocation")
	private String contentPathLocation;

	@SerializedName("albumArt")
	private String albumArt;

	@SerializedName("categoryNameAr")
	private String categoryNameAr;

	@SerializedName("artistName")
	private String artistName;

	@SerializedName("artistNameAr")
	private String artistNameAr;

	@SerializedName("songNameAr")
	private String songNameAr;

	@SerializedName("userId")
	private int userId;

	@SerializedName("approverId")
	private int approverId;

	@SerializedName("albumId")
	private int albumId;

	@SerializedName("createdDate")
	private String createdDate;

	@SerializedName("approvedDate")
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
}