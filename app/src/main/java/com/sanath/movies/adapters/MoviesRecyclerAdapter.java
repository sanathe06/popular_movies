package com.sanath.movies.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanath.movies.common.Util;
import com.sanath.movies.models.Api;
import com.sanath.movies.R;
import com.sanath.movies.common.OnMovieSelectListener;
import com.sanath.movies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanathnandasiri on 7/15/16.
 */

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerViewHolder> {

    private static final String TAG = MoviesRecyclerAdapter.class.getSimpleName();
    private List<Movie> mMovieList = new ArrayList<>();
    private final Context mContext;
    private final OnMovieSelectListener mOnMovieSelectListener;

    public MoviesRecyclerAdapter(Context context, List<Movie> movies, OnMovieSelectListener onMovieSelectListener) {
        this.mMovieList = movies;
        this.mContext = context;
        this.mOnMovieSelectListener = onMovieSelectListener;
    }

    @Override
    public MovieRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.movie_list_item, parent, false);
        return new MovieRecyclerViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final MovieRecyclerViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);
        Log.d(TAG,"Movie " + movie);
        Uri imageUri = Uri.parse(Api.BASE_URL_IMAGE).buildUpon()
                .appendPath("w185")
                .appendEncodedPath(movie.posterPath)
                .build();
        Log.d(TAG, imageUri.toString());
        Drawable drawableFilmReal;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //>= API 21
            drawableFilmReal = mContext.getResources().getDrawable(R.drawable.ic_film_real, mContext.getApplicationContext().getTheme());
        } else {
            drawableFilmReal = mContext.getResources().getDrawable(R.drawable.ic_film_real);
        }
        Picasso.with(mContext)
                .load(imageUri.toString())
                .placeholder(drawableFilmReal)
                .into(holder.mImageViewMovie);
        holder.mImageViewMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnMovieSelectListener.onSelectMovie(mMovieList.get(holder.getAdapterPosition()));
            }
        });

        holder.mTextView.setText(Util.getRating(movie.voteAverage));
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void add(List<Movie> movies) {
        mMovieList.clear();
        mMovieList.addAll(movies);
        notifyDataSetChanged();
    }

    public List<Movie> get() {
        return mMovieList;
    }
}
