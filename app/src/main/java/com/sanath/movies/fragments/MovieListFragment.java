package com.sanath.movies.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanath.movies.R;
import com.sanath.movies.activities.MoviesActivity;
import com.sanath.movies.adapters.MoviesRecyclerAdapter;
import com.sanath.movies.common.OnMovieSelectListener;
import com.sanath.movies.common.Util;
import com.sanath.movies.data.MovieContract;
import com.sanath.movies.models.Api;
import com.sanath.movies.models.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieListFragment extends BaseFragment {
    private static final String TAG = MovieListFragment.class.getSimpleName();

    private static final String KEY_SAVED_MOVIES = "KEY_SAVED_MOVIES";
    public static final String KEY_SELECTED_INDEX = "KEY_SELECTED_INDEX";

    private GridLayoutManager mGridLayoutManager;
    private MoviesRecyclerAdapter mMoviesRecyclerAdapter;
    private QueryMovieTask mQueryMoviesTask;

    private OnMovieSelectListener mMovieSelectListener;
    private Unbinder mUnBinder;
    @BindView(R.id.movie_grid)
    RecyclerView mRecyclerView;
    private int mSelectedIndex = 0;

    public MovieListFragment() {
        // Required empty public constructor
    }

    public static MovieListFragment newInstance(int selectedIndex) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_SELECTED_INDEX, selectedIndex);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_list_fragment, container, false);
        mUnBinder = ButterKnife.bind(this, rootView);
        int mColumnCount = getResources().getInteger(R.integer.num_columns);
        mGridLayoutManager = new GridLayoutManager(getActivity(), mColumnCount);

        assert mRecyclerView != null;
        setupRecyclerView(mRecyclerView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
        dismissSnackBar();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSelectedIndex = getArguments().getInt(KEY_SELECTED_INDEX);
        if (savedInstanceState != null) {
            //restore saved movie list
            mMoviesRecyclerAdapter.add((ArrayList) savedInstanceState.getParcelableArrayList(KEY_SAVED_MOVIES));
            mSelectedIndex = savedInstanceState.getInt(KEY_SELECTED_INDEX);
        } else {
            // new query
            queryMovies();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MoviesActivity moviesActivity = (MoviesActivity) context;
        if (!(moviesActivity != null)) {
            throw new IllegalStateException("you should implement OnMovieSelectListener");
        }

        mMovieSelectListener = moviesActivity;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_SAVED_MOVIES, (ArrayList<? extends Parcelable>) mMoviesRecyclerAdapter.get());
        outState.putInt(KEY_SELECTED_INDEX, mSelectedIndex);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        mMoviesRecyclerAdapter = new MoviesRecyclerAdapter(getActivity(), new ArrayList<Movie>(), mMovieSelectListener);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setAdapter(mMoviesRecyclerAdapter);
    }

    private void queryMovies() {
        if (mQueryMoviesTask != null) {
            if (mQueryMoviesTask.getStatus() == AsyncTask.Status.RUNNING) {
                mQueryMoviesTask.cancel(true);
            }
            mQueryMoviesTask = null;
        }
        mQueryMoviesTask = new QueryMovieTask();
        if (mSelectedIndex != 2) {
            if (Util.isOnline(getActivity())) {
                execute();
            } else {
                showSnackBar(R.string.msg_internet_connection_error, Snackbar.LENGTH_INDEFINITE, R.string.snackbar_action_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        queryMovies();
                    }
                });
            }
        }else{
            execute();
        }
    }

    private void execute() {
        dismissSnackBar();
        mQueryMoviesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSelectedIndex);
    }

    private class QueryMovieTask extends AsyncTask<Integer, Void, List<Movie>> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.msg_please_wait));
            progressDialog.show();
        }

        @Override
        protected List<Movie> doInBackground(Integer... params) {
            int selectedIndex = params[0];
            if (selectedIndex == 0) {
                return Api.getPopularMovies();
            } else if (selectedIndex == 1) {
                return Api.getTopRatedMovies();
            } else {
                return loadFavorite();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dismissProgressBar();
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                Log.i(TAG, "Movies : " + movies);
                mMoviesRecyclerAdapter.add(movies);
            } else {
                mMoviesRecyclerAdapter.add(new ArrayList<Movie>());
            }
            dismissProgressBar();
        }

        private void dismissProgressBar() {
            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                progressDialog = null;
            }
        }
    }

    private List<Movie> loadFavorite() {
        ArrayList<Movie> movies = new ArrayList<>();
        Cursor cursor = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            Movie movie;
            while (cursor.moveToNext()) {
                movie = MovieContract.MovieEntry.getMovieFromCursor(cursor);
                movies.add(movie);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return movies;
    }

}
