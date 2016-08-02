package com.sanath.movies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sanath.movies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sanathnandasiri on 8/2/16.
 */
public class ReviewsRecyclerViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textview_author)
    TextView mTextViewAuthor;
    @BindView(R.id.textview_content)
    TextView mTextViewContent;
    View mRootView;

    public ReviewsRecyclerViewHolder(View rootView) {
        super(rootView);
        ButterKnife.bind(this, rootView);
        mRootView = rootView;
    }
}
