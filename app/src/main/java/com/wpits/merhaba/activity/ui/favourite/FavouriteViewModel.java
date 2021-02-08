package com.wpits.merhaba.activity.ui.favourite;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class FavouriteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FavouriteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}