package com.sanath.movies.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanath.movies.models.Api;
import com.sanath.movies.R;
import com.sanath.movies.common.Util;
import com.sanath.movies.models.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MovieDetailsFragment extends Fragment {

    public static final String ARG_MOVIE = "item_id";
    private static final String TAG = MovieDetailsFragment.class.getSimpleName();

    private Movie mMovie;
    private Unbinder mUnbinder;
    @BindView(R.id.imageview_blackdrop) ImageView mImageViewBlackDrop;
    @BindView(R.id.textview_title) TextView mTextViewTitle;
    @BindView(R.id.textview_release_date) TextView mTextViewReleaseDate;
    @BindView(R.id.textview_rating) TextView mTextViewRating;
    @BindView(R.id.textview_overview) TextView mTextViewOverview;

    public MovieDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_MOVIE)) {
            mMovie = getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        if (mMovie != null) {
            Activity activity = this.getActivity();
            mTextViewTitle.setText(mMovie.title);
            mTextViewReleaseDate.setText(mMovie.releaseDate);
            String rating = Util.getRating(mMovie.voteAverage);
            if(rating != null){
                mTextViewRating.setText(String.format("%s/%s",rating,"10"));
            }else{
                mTextViewRating.setVisibility(View.GONE);
            }
            mTextViewOverview.setText(mMovie.overview);

            Uri imageUri = Uri.parse(Api.BASE_URL_IMAGE).buildUpon()
                    .appendPath("w500")
                    .appendEncodedPath(mMovie.backdropPath)
                    .build();
            Log.d(TAG, imageUri.toString());
            Picasso.with(activity)
                    .load(imageUri.toString())
                    .placeholder(R.drawable.place_holder_movie_poster)
                    .into(mImageViewBlackDrop);

        }


        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
