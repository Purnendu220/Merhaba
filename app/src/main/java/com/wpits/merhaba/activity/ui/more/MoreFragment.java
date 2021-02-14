package com.wpits.merhaba.activity.ui.more;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wpits.merhaba.R;
import com.wpits.merhaba.utils.ViewPagerFragmentSelection;

public class MoreFragment extends Fragment implements ViewPagerFragmentSelection {

    private MoreViewModel mViewModel;

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.more_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MoreViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onTabSelection(int position) {

    }
}