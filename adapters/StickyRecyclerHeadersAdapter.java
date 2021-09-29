package com.rogerthat.rlvltd.com.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by macbookair on 07/06/17.
 */

public interface StickyRecyclerHeadersAdapter<VH extends RecyclerView.ViewHolder> {
    public long getHeaderId(int position);

    public VH onCreateHeaderViewHolder(ViewGroup parent);

    public void onBindHeaderViewHolder(VH holder, int position);

    public int getItemCount();
}