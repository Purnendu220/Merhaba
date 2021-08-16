package com.wpits.merhaba.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wpits.merhaba.R;
import com.wpits.merhaba.adapter.viewholder.CategoryViewHoder;
import com.wpits.merhaba.adapter.viewholder.EmptyViewHolder;
import com.wpits.merhaba.adapter.viewholder.LoaderViewHolder;
import com.wpits.merhaba.adapter.viewholder.SongItemViewHolder;
import com.wpits.merhaba.model.ListLoader;
import com.wpits.merhaba.model.album.Song;

import java.util.ArrayList;
import java.util.List;




public class SongListAdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_UNKNOWN = -1;
    private static final int VIEW_TYPE_CLASS = 1;
    private static final int VIEW_TYPE_LOADER = 2;
    private static final int VIEW_TYPE_RECOMENDED = 4;

    private final AdapterCallbacks<Object> adapterCallbacks;

    private List<Object> list;
    private Context context;

    private int shownInScreen;

    private boolean showLoader;
    private ListLoader listLoader;

    public SongListAdapterNew(Context context, int shownInScreen, boolean showLoader, AdapterCallbacks<Object> adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        list = new ArrayList<>();
        this.shownInScreen = shownInScreen;
        this.showLoader = showLoader;

        listLoader = new ListLoader(true, context.getString(R.string.no_more_songs));
    }

    public List<Object> getList() {
        return list;
    }

    public void addClass(Song model) {
        list.add(model);
        addLoader();
    }

    public void addAllClass(List<Song> models) {
        list.addAll(models);
        addLoader();
    }



    public void clearAll() {
        list.clear();
    }

    public void loaderDone() {
        listLoader.setFinish(true);
        try {
            notifyItemChanged(list.indexOf(listLoader));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loaderReset() {
        listLoader.setFinish(false);
    }

    private void addLoader() {
        if (showLoader) {
            list.remove(listLoader);
            list.add(listLoader);
        }
    }

    @Override
    public int getItemViewType(int position) {

        int itemViewType = VIEW_TYPE_UNKNOWN;
        Object item = getItem(position);
        if (item instanceof Song) {
            itemViewType = VIEW_TYPE_CLASS;
        }

        else if (item instanceof ListLoader) {
            itemViewType = VIEW_TYPE_LOADER;
        }

        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CLASS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_horizontal_item_new, parent, false);
            return new SongItemViewHolder(view);

        } else if (viewType == VIEW_TYPE_LOADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new LoaderViewHolder(view);
        }
        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SongItemViewHolder) {
            if (getItem(position) != null)
                ((SongItemViewHolder) holder).bind(getItem(position), adapterCallbacks, position);

        } else if (holder instanceof CategoryViewHoder) {
            ((CategoryViewHoder) holder).bind(getItem(position), adapterCallbacks, position);

        } else if (holder instanceof LoaderViewHolder) {
            ((LoaderViewHolder) holder).bind(listLoader, adapterCallbacks);
            if (position == getItemCount() - 1 && !listLoader.isFinish()) {
                adapterCallbacks.onShowLastItem();
            }
        } else if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Object getItem(int position) {
        if (list != null && list.size() > 0)
            return list.get(position);
        else
            return null;
    }


}