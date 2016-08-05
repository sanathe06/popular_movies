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
    private boolean mTwoPane;
    private BaseFragment mMovieListFragment;
    Spinner mNavigationSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setupNavigationSpinner();

        mTwoPane = findViewById(R.id.movie_detail_container) != null;
    }

    private void setupNavigationSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, R.layout.spinner_title_item);
        adapter.setDropDownViewResource(R.layout.spinner_drop_item);

        mNavigationSpinner = (Spinner) findViewById(R.id.spinner_movies);
        mNavigationSpinner.setAdapter(adapter);
        mNavigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                loadMovieListFragment(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
            // TODO: 8/5/2016 have to load spinner for default sort order
            Util.setPreferencesChange(this, false);
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
