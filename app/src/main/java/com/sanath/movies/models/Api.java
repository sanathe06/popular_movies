package com.sanath.movies.models;

import android.util.Log;

import com.sanath.movies.BuildConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Sanath Nandasiri on 7/17/2016.
 */

public class Api {
    private static final String TAG = Api.class.getSimpleName();
    public static final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p";
    private static final String BASE_URL_API = "https://api.themoviedb.org/3/";
    private static final String popular_movies = "popular";
    private static final String top_rated_movies = "top_rated";


    public static List<Movie> getPopularMovies() {
        return getMovies(popular_movies);
    }

    public static List<Movie> getTopRatedMovies() {
        return getMovies(top_rated_movies);
    }

    public static List<Trailer> getTailers(String movieId) {
        MovieDbService service = getMovieDbService();
        Call<Trailers> call = service.listTrailers(movieId, BuildConfig.API_KEY);
        try {
            retrofit2.Response<Trailers> response = call.execute();
            Trailers trailers = response.body();
            return trailers.getTrailers();
        } catch (IOException e) {
            Log.e(TAG, "error occur while query trailers for movie id " + movieId, e);
        }
        return new ArrayList<>();
    }

    public static List<Review> getReviews(String movieId) {
        MovieDbService service = getMovieDbService();
        Call<Reviews> call = service.listReviews(movieId, BuildConfig.API_KEY);
        try {
            retrofit2.Response<Reviews> response = call.execute();
            Reviews reviews = response.body();
            return reviews.getReviews();
        } catch (IOException e) {
            Log.e(TAG, "error occur while query reviews for movie id " + movieId, e);
        }
        return new ArrayList<>();
    }

    private static List<Movie> getMovies(String sortBy) {

        MovieDbService service = getMovieDbService();
        Call<Movies> call = service.listMovies(sortBy, BuildConfig.API_KEY);
        try {
            retrofit2.Response<Movies> response = call.execute();
            Movies movies = response.body();
            return movies.getMovies();
        } catch (IOException e) {
            Log.e(TAG, "error occur while query movies ", e);
        }
        return new ArrayList<>();
    }

    private static MovieDbService getMovieDbService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(MovieDbService.class);
    }
}
