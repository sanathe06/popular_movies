package com.sanath.movies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanath.movies.R;
import com.sanath.movies.common.Util;
import com.sanath.movies.models.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sanathnandasiri on 7/28/16.
 */

public class TrailersRecyclerAdapter extends RecyclerView.Adapter<TrailerRecyclerViewHolder> {

    private static final String TAG = TrailersRecyclerAdapter.class.getSimpleName();
    private ArrayList<Trailer> mTrailerList = new ArrayList<>();
    private final Context mContext;

    public TrailersRecyclerAdapter(Context context, ArrayList<Trailer> trailers) {
        this.mTrailerList = trailers;
        this.mContext = context;
    }

    @Override
    public TrailerRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerRecyclerViewHolder(root);
    }

    @Override
    public void onBindViewHolder(TrailerRecyclerViewHolder holder, int position) {
        final Trailer trailer = mTrailerList.get(position);
        Log.d(TAG, "Trailer : " + trailer);
        Picasso.with(mContext)
                .load(trailer.getThumbnailUrl())
                .into(holder.mImageViewThumbnail);
        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.openYoutubeVideo(mContext, trailer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }

    public void add(ArrayList<Trailer> trailers) {
        mTrailerList.clear();
        mTrailerList.addAll(trailers);
        notifyDataSetChanged();
    }

    public ArrayList<Trailer> get() {
        return mTrailerList;
    }
}
