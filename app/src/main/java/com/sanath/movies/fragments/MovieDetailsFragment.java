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


public class MovieDetailsFragment extends Fragment {

    public static final String ARG_MOVIE = "item_id";
    private static final String TAG = MovieDetailsFragment.class.getSimpleName();

    private Movie mMovie;

    public MovieDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        if (getArguments().containsKey(ARG_MOVIE)) {
            mMovie = getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);
        if (mMovie != null) {
            Activity activity = this.getActivity();

            ImageView imageViewBlackDrop = (ImageView) rootView.findViewById(R.id.blackdrop_image_view);
            TextView textViewTitle = (TextView) rootView.findViewById(R.id.textViewTitle);
            TextView textViewReleaseDate = (TextView) rootView.findViewById(R.id.textViewReleaseDate);
            TextView textViewRating = (TextView) rootView.findViewById(R.id.textViewRating);
            TextView textViewOverview = (TextView) rootView.findViewById(R.id.textViewOverview);

            textViewTitle.setText(mMovie.title);
            textViewReleaseDate.setText(mMovie.releaseDate);
            String rating = Util.getRating(mMovie.voteAverage);
            if(rating != null){
                textViewRating.setText(String.format("%s/%s",rating,"10"));
            }else{
                textViewRating.setVisibility(View.GONE);
            }
            textViewOverview.setText(mMovie.overview);

            Uri imageUri = Uri.parse(Api.BASE_URL_IMAGE).buildUpon()
                    .appendPath("w500")
                    .appendEncodedPath(mMovie.backdropPath)
                    .build();
            Log.d(TAG, imageUri.toString());
            Picasso.with(activity)
                    .load(imageUri.toString())
                    .placeholder(R.drawable.place_holder_movie_poster)
                    .into(imageViewBlackDrop);

        }


        return rootView;
    }
}
