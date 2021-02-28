package com.wpits.merhaba.activity.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.wpits.merhaba.R;
import com.wpits.merhaba.activity.HomeActivityNew;
import com.wpits.merhaba.activity.SplashActivity;
import com.wpits.merhaba.databinding.FragmentFavouriteBinding;
import com.wpits.merhaba.databinding.FragmentSettingsBinding;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.utility.Utility;
import com.wpits.merhaba.utils.AppConstant;
import com.wpits.merhaba.utils.ViewPagerFragmentSelection;

import phonenumberui.PhoneNumberActivity;


public class SettingsFragment extends Fragment implements ViewPagerFragmentSelection {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;
    private Context mContext;
    boolean isArabic = Utility.isArabic();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SettingsViewModel.class);
        binding = FragmentSettingsBinding.inflate(inflater,container,false);
        mContext = getActivity();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(isArabic){
            binding.radioArabic.setChecked(true);
        }else{
            binding.radioEnglish.setChecked(true);

        }
        binding.radioArabic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setUserDefaultLanguage(AppConstant.Language.ARABIC);

            }
        });
        binding.radioEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setUserDefaultLanguage(AppConstant.Language.ENGLISH);
            }
        });
    }

    private void setUserDefaultLanguage(String language){
        PrefrenceManager.getInstance().setUserLanguage(language);

            Intent i = new Intent(mContext, SplashActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            getActivity().finish();


    }

    @Override
    public void onTabSelection(int position) {

    }
}