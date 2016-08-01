package com.sanath.movies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sanath.movies.R;
import com.sanath.movies.common.OnMovieSelectListener;
import com.sanath.movies.common.Util;
import com.sanath.movies.fragments.BaseFragment;
import com.sanath.movies.fragments.MovieDetailsFragment;
import com.sanath.movies.fragments.MovieListFragment;
import com.sanath.movies.models.Movie;

public class MoviesActivity extends AppCompatActivity implements OnMovieSelectListener {

    private static final String TAG_DETAILS_FRAGMENT = "TAG_DETAILS_FRAGMENT";
    private static final String TAG_MOVIE_LIST_FRAGMENT = "TAG_MOVIE_LIST_FRAGMENT";
    private boolean mTwoPane;
    private BaseFragment mMovieListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        mTwoPane = findViewById(R.id.movie_detail_container) != null;

        mMovieListFragment = (BaseFragment) getSupportFragmentManager()
                .findFragmentByTag(TAG_MOVIE_LIST_FRAGMENT);
        if (mMovieListFragment == null) {
            loadMovieListFragment();
        }
    }

    private void loadMovieListFragment() {
        mMovieListFragment = new MovieListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_list_container, mMovieListFragment, TAG_MOVIE_LIST_FRAGMENT)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.isPreferencesChanged(this)) {
            loadMovieListFragment();
            Util.setPreferencesChange(this,false);
        }
        setTitle(getTitle((Util.getDefaultSortOrder(this))));
    }

    private CharSequence getTitle(String sortOrder) {
        if(sortOrder.equals("popular")){
            return getString(R.string.title_poplar_movies);
        }else{
            return getString(R.string.title_movies_top_rated);
        }
    }

    @Override
    public void onSelectMovie(Movie movie) {
        if (mTwoPane) {
            showMovieDetail(movie);
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailsFragment.ARG_MOVIE, movie);
            startActivity(intent);
        }
    }

    private void showMovieDetail(Movie movie) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(MovieDetailsFragment.ARG_MOVIE, movie);
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_detail_container, fragment, TAG_DETAILS_FRAGMENT)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ic_action_setting) {
            startSettingActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSettingActivity() {
        startActivity(new Intent(this, SettingActivity.class));
    }
}
