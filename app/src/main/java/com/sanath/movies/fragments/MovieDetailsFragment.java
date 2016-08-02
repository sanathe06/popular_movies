package com.sanath.movies.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sanath.movies.adapters.ReviewsRecyclerAdapter;
import com.sanath.movies.adapters.TrailersRecyclerAdapter;
import com.sanath.movies.models.Api;
import com.sanath.movies.R;
import com.sanath.movies.common.Util;
import com.sanath.movies.models.Movie;
import com.sanath.movies.models.Review;
import com.sanath.movies.models.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MovieDetailsFragment extends BaseFragment {

    public static final String ARG_MOVIE = "args_movie";
    private static final String TAG = MovieDetailsFragment.class.getSimpleName();
    public static final String KEY_TRAILERS = "trailers";
    public static final String KEY_REVIEWS = "reviews";

    private Movie mMovie;
    private Unbinder mUnBinder;
    @BindView(R.id.imageview_blackdrop)
    ImageView mImageViewBlackDrop;
    @BindView(R.id.textview_title)
    TextView mTextViewTitle;
    @BindView(R.id.textview_release_date)
    TextView mTextViewReleaseDate;
    @BindView(R.id.textview_rating)
    TextView mTextViewRating;
    @BindView(R.id.textview_overview)
    TextView mTextViewOverview;
    @BindView(R.id.recyclerview_trailers)
    RecyclerView mRecyclerViewTrailers;
    @BindView(R.id.recyclerview_reviews)
    RecyclerView mRecyclerViewReviews;
    @BindView(R.id.progressbar_trailers)
    ProgressBar mProgressBarTrailers;
    @BindView(R.id.progressbar_reviews)
    ProgressBar mProgressBarReviews;

    private TrailersRecyclerAdapter mTrailersRecyclerAdapter;
    private QueryTrailersTask mQueryTrailersTask;
    private ReviewsRecyclerAdapter mReviewsRecyclerAdapter;
    private QueryReviewsTask mQueryReviewsTask;

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
        mUnBinder = ButterKnife.bind(this, rootView);
        if (mMovie != null) {
            Activity activity = this.getActivity();
            mTextViewTitle.setText(mMovie.title);
            mTextViewReleaseDate.setText(mMovie.releaseDate);
            String rating = Util.getRating(mMovie.voteAverage);
            if (rating != null) {
                mTextViewRating.setText(String.format("%s/%s", rating, "10"));
            } else {
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
        mTrailersRecyclerAdapter = new TrailersRecyclerAdapter(getActivity(), new ArrayList<Trailer>());
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewTrailers.setLayoutManager(layoutManager);
        mRecyclerViewTrailers.setAdapter(mTrailersRecyclerAdapter);

        mReviewsRecyclerAdapter = new ReviewsRecyclerAdapter(getActivity(), new ArrayList<Review>());
        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerViewReviews.setLayoutManager(layoutManager2);
        mRecyclerViewReviews.setAdapter(mReviewsRecyclerAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            mTrailersRecyclerAdapter.add(savedInstanceState.<Trailer>getParcelableArrayList(KEY_TRAILERS));
            mReviewsRecyclerAdapter.add(savedInstanceState.<Review>getParcelableArrayList(KEY_REVIEWS));
        } else {
            loadTrailers(mMovie.id);
            loadReviews(mMovie.id);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_TRAILERS, mTrailersRecyclerAdapter.get());
        outState.putParcelableArrayList(KEY_REVIEWS, mReviewsRecyclerAdapter.get());
    }


    private void loadReviews(final String movieId) {
        if (mQueryReviewsTask != null) {
            if (mQueryReviewsTask.getStatus() == AsyncTask.Status.RUNNING) {
                mQueryReviewsTask.cancel(true);
            }
            mQueryReviewsTask = null;
        }
        mQueryReviewsTask = new QueryReviewsTask();
        if (Util.isOnline(getActivity())) {
            dismissSnackBar();
            mQueryReviewsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, movieId);
        } else {
            showSnackBar(R.string.msg_internet_connection_error, Snackbar.LENGTH_INDEFINITE, R.string.snackbar_action_retry, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadTrailers(movieId);
                }
            });
        }
    }

    private void loadTrailers(final String movieId) {
        if (mQueryTrailersTask != null) {
            if (mQueryTrailersTask.getStatus() == AsyncTask.Status.RUNNING) {
                mQueryTrailersTask.cancel(true);
            }
            mQueryTrailersTask = null;
        }
        mQueryTrailersTask = new QueryTrailersTask();
        if (Util.isOnline(getActivity())) {
            dismissSnackBar();
            mQueryTrailersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, movieId);
        } else {
            showSnackBar(R.string.msg_internet_connection_error, Snackbar.LENGTH_INDEFINITE, R.string.snackbar_action_retry, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadTrailers(movieId);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }

    private class QueryTrailersTask extends AsyncTask<String, Void, List<Trailer>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBarTrailers.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Trailer> doInBackground(String... strings) {
            String movieId = strings[0];
            if (movieId == null) return null;
            return Api.getTailers(movieId);
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            super.onPostExecute(trailers);
            mProgressBarTrailers.setVisibility(View.GONE);
            if (trailers != null) {
                Log.i(TAG, "Trailers : " + trailers);
                mTrailersRecyclerAdapter.add((ArrayList<Trailer>) trailers);
            } else {
                mTrailersRecyclerAdapter.add(new ArrayList<Trailer>());
            }
        }
    }

    private class QueryReviewsTask extends AsyncTask<String, Void, List<Review>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBarReviews.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Review> doInBackground(String... strings) {
            String movieId = strings[0];
            if (movieId == null) return null;
            return Api.getReviews(movieId);
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            super.onPostExecute(reviews);
            mProgressBarReviews.setVisibility(View.GONE);
            if (reviews != null) {
                Log.i(TAG, "Reviews : " + reviews);
                mReviewsRecyclerAdapter.add((ArrayList<Review>) reviews);
            } else {
                mReviewsRecyclerAdapter.add(new ArrayList<Review>());
            }
        }
    }
}
