package com.wpits.merhaba.adapter.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wpits.merhaba.R;
import com.wpits.merhaba.adapter.AdapterCallbacks;
import com.wpits.merhaba.model.ListLoader;


/**
 * Created by MyU10 on 3/10/2018.
 */

public class LoaderViewHolder extends RecyclerView.ViewHolder {


    private Context context;
    public ProgressBar progressBar;
    public TextView text;
    public LoaderViewHolder(View view) {
        super(view);

        context = view.getContext();
        progressBar =  view.findViewById(R.id.progressBar);
        text = view.findViewById(R.id.text);
    }

    public void bind(ListLoader model, final AdapterCallbacks adapterCallbacks) {

        if (model.isFinish()) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        if (model.isShowText()) {

            text.setVisibility(View.VISIBLE);

            if (model.isFinish())
                text.setText(model.getFinishText());
            else
                text.setText(model.getLoadingText());

        } else {
            text.setVisibility(View.GONE);
            text.setText("");
        }
    }
}
