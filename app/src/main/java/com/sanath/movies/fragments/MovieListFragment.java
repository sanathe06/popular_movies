package com.sanath.movies.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanath.movies.R;
import com.sanath.movies.activities.MoviesActivity;
import com.sanath.movies.adapters.MoviesRecyclerAdapter;
import com.sanath.movies.common.OnMovieSelectListener;
import com.sanath.movies.common.Util;
import com.sanath.movies.models.Api;
import com.sanath.movies.models.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieListFragment extends BaseFragment {

    private static final String KEY_SAVED_MOVIES = "key_saved_movies";

    private GridLayoutManager mGridLayoutManager;
    private MoviesRecyclerAdapter mMoviesRecyclerAdapter;
    private final List<Movie> mMovieList = new ArrayList<>();
    private AsyncTask<String, Void, List<Movie>> mQueryTask;

    private OnMovieSelectListener mMovieSelectListener;
    private Unbinder mUnBinder;
    @BindView(R.id.movie_grid)
    RecyclerView mRecyclerView;

    public MovieListFragment() {
        // Required empty public constructor
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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            //restore saved movie list
            mMovieList.clear();
            mMovieList.addAll((ArrayList) savedInstanceState.getParcelableArrayList(KEY_SAVED_MOVIES));
            mMoviesRecyclerAdapter.notifyDataSetChanged();
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
        outState.putParcelableArrayList(KEY_SAVED_MOVIES, (ArrayList<? extends Parcelable>) mMovieList);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        mMoviesRecyclerAdapter = new MoviesRecyclerAdapter(getActivity(), mMovieList, mMovieSelectListener);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setAdapter(mMoviesRecyclerAdapter);
    }

    private void queryMovies() {
        if (mQueryTask != null) {
            if (mQueryTask.getStatus() == AsyncTask.Status.RUNNING) {
                mQueryTask.cancel(true);
            }
            mQueryTask = null;
        }
        mQueryTask = new QueryMovieTask();
        if (Util.isOnline(getActivity())) {
            dismissSnackBar();
            mQueryTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getDefaultSortOrder());
        } else {
            showSnackBar(R.string.msg_internet_connection_error, Snackbar.LENGTH_INDEFINITE, R.string.snackbar_action_retry, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    queryMovies();
                }
            });
        }
    }

    private String getDefaultSortOrder() {
        return Util.getDefaultSortOrder(getActivity());
    }

    private class QueryMovieTask extends AsyncTask<String, Void, List<Movie>> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.msg_please_wait));
            progressDialog.show();
            mMovieList.clear();
            mMoviesRecyclerAdapter.notifyDataSetChanged();
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            if (params[0].equals("popular")) {
                return Api.getPopularMovies();
            } else {
                return Api.getTopRatedMovies();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dismissProgressBar();
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            mMovieList.addAll(movies);
            mMoviesRecyclerAdapter.notifyDataSetChanged();
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

}
