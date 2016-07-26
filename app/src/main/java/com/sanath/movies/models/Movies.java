package com.sanath.movies.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sanathnandasiri on 7/27/16.
 */

public class Movies {
    @SerializedName("results")
    ArrayList<Movie> movies = new ArrayList<>();

    public Movies() {
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }
}
