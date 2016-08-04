package com.sanath.movies.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sanath.movies.adapters.ReviewsRecyclerAdapter;
import com.sanath.movies.adapters.TrailersRecyclerAdapter;
import com.sanath.movies.data.MovieContract;
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
    private static final String TAG = MovieDetailsFragment.class.getSimpleName();

    public static final String ARG_MOVIE = "args_movie";
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
    @BindView(R.id.fabFavorite)
    FloatingActionButton mActionButtonFavorite;

    private TrailersRecyclerAdapter mTrailersRecyclerAdapter;
    private QueryTrailersTask mQueryTrailersTask;
    private ReviewsRecyclerAdapter mReviewsRecyclerAdapter;
    private QueryReviewsTask mQueryReviewsTask;

    public MovieDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
            checkForFavorite();

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

        mActionButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMovie.isFavorite) return;
                makeThisFavorite(mMovie);
            }
        });
        return rootView;
    }

    private void showFavoriteIcon(boolean isFavorite) {
        Drawable drawable;
        if (isFavorite) {
            drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_heart);
        } else {
            drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_heart_outline);
        }
        mActionButtonFavorite.setImageDrawable(drawable);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_menu, menu);
        if (mTrailersRecyclerAdapter.get() == null || mTrailersRecyclerAdapter.get().size() <= 0) {
            menu.removeItem(R.id.ic_action_share);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ic_action_share) {
            if (mTrailersRecyclerAdapter.get() != null && mTrailersRecyclerAdapter.get().size() > 0) {
                String videoUrl = mTrailersRecyclerAdapter.get().get(0).getVideoUrl();
                shareUrl(mMovie.originalTitle, videoUrl);
            } else {
                Toast.makeText(getActivity(), R.string.msg_no_trailers_to_share, Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareUrl(String subject, String content) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, subject);
        share.putExtra(Intent.EXTRA_TEXT, content);

        startActivity(Intent.createChooser(share, getString(R.string.title_share)));
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
            getActivity().invalidateOptionsMenu();
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

    private void makeThisFavorite(final Movie mMovie) {
        new AsyncTask<Movie, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Movie... movies) {
                ContentValues values = MovieContract.MovieEntry.getContentValues(movies[0]);
                try {
                    Uri uri = getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
                    if (uri != null) {
                        return true;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Make favorite failed ", e);
                    return false;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                setFavorite(aBoolean, mMovie);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMovie);
    }

    private void setFavorite(Boolean aBoolean, Movie mMovie) {
        mMovie.isFavorite = aBoolean;
        showFavoriteIcon(mMovie.isFavorite);
    }


    private void checkForFavorite() {
        new AsyncTask<Movie, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Movie... movies) {
                Cursor cursor = getContext().getContentResolver().query(
                        MovieContract.MovieEntry.buildMovieUri(Long.valueOf(movies[0].id)),
                        new String[]{MovieContract.MovieEntry._ID},
                        null,
                        null,
                        null
                );
                if (cursor != null && cursor.moveToFirst()) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                setFavorite(aBoolean, mMovie);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMovie);
    }
}
