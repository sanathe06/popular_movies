package com.sanath.movies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.sanath.movies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sanathnandasiri on 7/28/16.
 */
public class TrailerRecyclerViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageview_trailer)
    ImageView mImageViewThumbnail;
    View mRootView;

    public TrailerRecyclerViewHolder(View rootView) {
        super(rootView);
        ButterKnife.bind(this, rootView);
        mRootView = rootView;
    }
}
