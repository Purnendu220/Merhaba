package com.wpits.merhaba.model;

import com.wpits.merhaba.model.category.Category;

import java.util.List;

public class CategoryListModel {
    private List<Category> categories;


    public CategoryListModel(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
