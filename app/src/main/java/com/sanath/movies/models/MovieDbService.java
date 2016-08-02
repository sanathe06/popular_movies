package com.sanath.movies.models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sanathnandasiri on 7/27/16.
 */

public interface MovieDbService {

    @GET("movie/{sort_by}")
    Call<Movies> listMovies(@Path("sort_by") String sortBy, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<Trailers> listTrailers(@Path("movie_id") String movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<Reviews> listReviews(@Path("movie_id") String movieId, @Query("api_key") String apiKey);
}
