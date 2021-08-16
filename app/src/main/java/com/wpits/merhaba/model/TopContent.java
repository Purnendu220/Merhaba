package com.wpits.merhaba.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopContent {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("songId")
@Expose
private Integer songId;
@SerializedName("songName")
@Expose
private String songName;
@SerializedName("category")
@Expose
private String category;
@SerializedName("categoryId")
@Expose
private Integer categoryId;
@SerializedName("order")
@Expose
private String order;
@SerializedName("uploaderType")
@Expose
private Integer uploaderType;
@SerializedName("isActive")
@Expose
private Integer isActive;
@SerializedName("contentPathLocation")
@Expose
private String contentPathLocation;
@SerializedName("albumArt")
@Expose
private String albumArt;
@SerializedName("categoryNameAr")
@Expose
private String categoryNameAr;
@SerializedName("artistName")
@Expose
private String artistName;
@SerializedName("artistNameAr")
@Expose
private String artistNameAr;
@SerializedName("songNameAr")
@Expose
private String songNameAr;
@SerializedName("userId")
@Expose
private Integer userId;
@SerializedName("approverId")
@Expose
private Integer approverId;
@SerializedName("albumId")
@Expose
private Integer albumId;
@SerializedName("createdDate")
@Expose
private String createdDate;
@SerializedName("approvedDate")
@Expose
private String approvedDate;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public Integer getSongId() {
return songId;
}

public void setSongId(Integer songId) {
this.songId = songId;
}

public String getSongName() {
return songName;
}

public void setSongName(String songName) {
this.songName = songName;
}

public String getCategory() {
return category;
}

public void setCategory(String category) {
this.category = category;
}

public Integer getCategoryId() {
return categoryId;
}

public void setCategoryId(Integer categoryId) {
this.categoryId = categoryId;
}

public String getOrder() {
return order;
}

public void setOrder(String order) {
this.order = order;
}

public Integer getUploaderType() {
return uploaderType;
}

public void setUploaderType(Integer uploaderType) {
this.uploaderType = uploaderType;
}

public Integer getIsActive() {
return isActive;
}

public void setIsActive(Integer isActive) {
this.isActive = isActive;
}

public String getContentPathLocation() {
return contentPathLocation;
}

public void setContentPathLocation(String contentPathLocation) {
this.contentPathLocation = contentPathLocation;
}

public String getAlbumArt() {
return albumArt;
}

public void setAlbumArt(String albumArt) {
this.albumArt = albumArt;
}

public String getCategoryNameAr() {
return categoryNameAr;
}

public void setCategoryNameAr(String categoryNameAr) {
this.categoryNameAr = categoryNameAr;
}

public String getArtistName() {
return artistName;
}

public void setArtistName(String artistName) {
this.artistName = artistName;
}

public String getArtistNameAr() {
return artistNameAr;
}

public void setArtistNameAr(String artistNameAr) {
this.artistNameAr = artistNameAr;
}

public String getSongNameAr() {
return songNameAr;
}

public void setSongNameAr(String songNameAr) {
this.songNameAr = songNameAr;
}

public Integer getUserId() {
return userId;
}

public void setUserId(Integer userId) {
this.userId = userId;
}

public Integer getApproverId() {
return approverId;
}

public void setApproverId(Integer approverId) {
this.approverId = approverId;
}

public Integer getAlbumId() {
return albumId;
}

public void setAlbumId(Integer albumId) {
this.albumId = albumId;
}

public String getCreatedDate() {
return createdDate;
}

public void setCreatedDate(String createdDate) {
this.createdDate = createdDate;
}

public String getApprovedDate() {
return approvedDate;
}

public void setApprovedDate(String approvedDate) {
this.approvedDate = approvedDate;
}

}