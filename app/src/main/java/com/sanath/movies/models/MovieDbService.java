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
}
