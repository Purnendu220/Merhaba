package com.wpits.merhaba.model;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("topContent")
@Expose
private TopContent topContent;
@SerializedName("rating")
@Expose
private Integer rating;
@SerializedName("remarks")
@Expose
private String remarks;
@SerializedName("createDate")
@Expose
private String createDate;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public TopContent getTopContent() {
return topContent;
}

public void setTopContent(TopContent topContent) {
this.topContent = topContent;
}

public Integer getRating() {
return rating;
}

public void setRating(Integer rating) {
this.rating = rating;
}

public String getRemarks() {
return remarks;
}

public void setRemarks(String remarks) {
this.remarks = remarks;
}

public String getCreateDate() {
return createDate;
}

public void setCreateDate(String createDate) {
this.createDate = createDate;
}

}





