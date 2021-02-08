
package com.wpits.merhaba.model.category;


import java.util.ArrayList;

public class Category {

    private ArrayList allItemsInSection;

    private Integer id;

    private String categoryName;

    private String categoryNameAr;

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

    public ArrayList getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList allItemsInSection) {
        this.allItemsInSection = allItemsInSection;

    }

}
