package com.sanath.movies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanath.movies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sanathnandasiri on 7/15/16.
 */
public class MovieRecyclerViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.movie_grid_image_view) ImageView mImageViewMovie;
    @BindView(R.id.movie_item_rating_textview) TextView mTextView;

    public MovieRecyclerViewHolder(View root) {
        super(root);
        ButterKnife.bind(this,root);
    }
}
