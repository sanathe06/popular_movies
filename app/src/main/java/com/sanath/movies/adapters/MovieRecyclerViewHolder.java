package com.sanath.movies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanath.movies.R;

/**
 * Created by sanathnandasiri on 7/15/16.
 */
public class MovieRecyclerViewHolder extends RecyclerView.ViewHolder{
    public final ImageView mImageViewMovie;
    public final TextView mTextView;

    public MovieRecyclerViewHolder(View root) {
        super(root);
        mImageViewMovie = (ImageView) root.findViewById(R.id.movie_grid_image_view);
        mTextView = (TextView) root.findViewById(R.id.movie_item_rating_textview);
    }
}
