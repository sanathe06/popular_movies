package com.sanath.movies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
    private static final String KEY_SPINNER_SELECTED_POSITION = "KEY_SPINNER_SELECTED_POSITION";
    private boolean mTwoPane;
    private BaseFragment mMovieListFragment;
    private Spinner mNavigationSpinner;
    private int mNavigationSpinnerSelectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        mNavigationSpinner = (Spinner) findViewById(R.id.spinner_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (savedInstanceState != null) {
            mNavigationSpinnerSelectedPosition = savedInstanceState.getInt(KEY_SPINNER_SELECTED_POSITION);
        } else {
            //load default mNavigationSpinnerSelectedPosition
            mNavigationSpinnerSelectedPosition = getDefaultSortOrderPosition();
            loadMovieListFragment(mNavigationSpinnerSelectedPosition);
        }
        setupNavigationSpinner();

        setNavigationSpinnerSelection(mNavigationSpinnerSelectedPosition);

        mTwoPane = findViewById(R.id.movie_detail_container) != null;
    }

    private void setNavigationSpinnerSelection(final int selectedPosition) {
        mNavigationSpinner.setOnItemSelectedListener(null);
        mNavigationSpinner.setSelection(selectedPosition);
        mNavigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (mNavigationSpinnerSelectedPosition != position) {
                    loadMovieListFragment(position);
                }
                mNavigationSpinnerSelectedPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupNavigationSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, R.layout.spinner_title_item);
        adapter.setDropDownViewResource(R.layout.spinner_drop_item);
        mNavigationSpinner.setAdapter(adapter);
    }

    private void loadMovieListFragment(int selectedIndex) {
        mMovieListFragment = MovieListFragment.newInstance(selectedIndex);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_list_container, mMovieListFragment, TAG_MOVIE_LIST_FRAGMENT)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.isPreferencesChanged(this)) {
            mNavigationSpinnerSelectedPosition = getDefaultSortOrderPosition();
            setNavigationSpinnerSelection(mNavigationSpinnerSelectedPosition);
            loadMovieListFragment(mNavigationSpinnerSelectedPosition);
            Util.setPreferencesChange(this, false);
        }
    }

    private int getDefaultSortOrderPosition() {
        String defaultSortOrder = Util.getDefaultSortOrder(this);
        if (defaultSortOrder.equals("popular")) {
            return 0;
        } else if (defaultSortOrder.equals("top_rated")) {
            return 1;
        } else {
            return 2;
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SPINNER_SELECTED_POSITION, mNavigationSpinnerSelectedPosition);
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
