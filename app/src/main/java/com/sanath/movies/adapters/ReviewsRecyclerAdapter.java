package com.sanath.movies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanath.movies.R;
import com.sanath.movies.common.Util;
import com.sanath.movies.models.Review;

import java.util.ArrayList;

/**
 * Created by sanathnandasiri on 8/2/16.
 */
public class ReviewsRecyclerAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewHolder> {
    private static final String TAG = ReviewsRecyclerAdapter.class.getSimpleName();
    private final Context mContext;
    private ArrayList<Review> mReviews = new ArrayList<>();

    public ReviewsRecyclerAdapter(Context context, ArrayList<Review> reviews) {
        this.mContext = context;
        this.mReviews = reviews;
    }

    @Override
    public ReviewsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.reviews_list_item, parent, false);
        return new ReviewsRecyclerViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ReviewsRecyclerViewHolder holder, int position) {
        final Review review = mReviews.get(position);
        Log.d(TAG, "Review : " + review);
        holder.mTextViewAuthor.setText(review.author);
        holder.mTextViewContent.setText(review.content);
        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.openWebPage(mContext, review.url
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void add(ArrayList<Review> reviews) {
        mReviews.clear();
        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }

    public ArrayList<Review> get() {
        return mReviews;
    }
}
