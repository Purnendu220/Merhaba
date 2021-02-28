
package com.wpits.merhaba.model.album;


import java.io.Serializable;

public class Song implements Serializable {


    private Integer songId;

    private String songName;

    private String songsNameAr;

    private String songNameAr;



    private String contentPathLocation;

    private String artistName;

    private String artistNameAr;

    private String albumArt;

    private Integer id;

    private String categoryName;

    private String categoryNameAr;

    private boolean favStatus;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    private Integer categoryId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryNameAr() {
        return categoryNameAr;
    }

    public void setCategoryNameAr(String categoryNameAr) {
        this.categoryNameAr = categoryNameAr;
    }



    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getContentPathLocation() {
        return contentPathLocation;
    }

    public void setContentPathLocation(String contentPathLocation) {
        this.contentPathLocation = contentPathLocation;
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

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }



    public Integer getSongId() {
        return songId;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }

    public String getSongNameEn() {
        return songName;
    }

    public void setSongNameEn(String songNameEn) {
        this.songName = songNameEn;
    }

    public String getSongsNameAr() {
        return songsNameAr;
    }

    public void setSongsNameAr(String songsNameAr) {
        this.songsNameAr = songsNameAr;
    }

    public boolean getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(boolean favStatus) {
        this.favStatus = favStatus;
    }

    public String getSongNameAr() {
        return songNameAr;
    }

    public void setSongNameAr(String songNameAr) {
        this.songNameAr = songNameAr;
    }

    public boolean isFavStatus() {
        return favStatus;
    }
}
