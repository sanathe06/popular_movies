package com.sanath.movies.models;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sanath.movies.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Sanath Nandasiri on 7/17/2016.
 */

public class Api {
    private static final String TAG = Api.class.getSimpleName();
    private static final String API_KEY = "api_key";
    public static final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p";
    private static final String BASE_URL_API = "https://api.themoviedb.org/3/movie/";
    private static final String popular_movies = "popular";
    private static final String top_rated_movies = "top_rated";


    public static List<Movie> getPopularMovies() {
        Uri buildUri = Uri.parse(BASE_URL_API)
                .buildUpon()
                .appendPath(popular_movies)
                .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                .build();
        return getMovies(buildUri.toString());
    }

    public static List<Movie> getTopRatedMovies() {
        Uri buildUri = Uri.parse(BASE_URL_API)
                .buildUpon()
                .appendPath(top_rated_movies)
                .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                .build();
        return getMovies(buildUri.toString());
    }

    private static List<Movie> getMovies(String url) {
        List<Movie> movies = new ArrayList<>();

        String data;
        Response response;
        try {
            response = get(url);
            if (response.code() == 200) {
                if (response.body() != null) {
                    data = response.body().string();
                    JSONObject  responseData = new JSONObject(data);
                    if (responseData.has("results")) {
                        data = responseData.get("results").toString();
                    }else{
                        return movies;
                    }
                    Gson gson = new Gson();
                    Type collectionType = new TypeToken<Collection<Movie>>() {}.getType();
                    Collection<Movie> movieCollection = gson.fromJson(data, collectionType);
                    movies = (List<Movie>) movieCollection;
                } else {
                    Log.e(TAG, "Response body is empty");
                }
            } else {
                Log.e(TAG, "Response not valid Status code " + response.code());
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return movies;
    }

    private static Response get(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        return client.newCall(request).execute();
    }
}
