package com.sanath.movies.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sanathnandasiri on 7/28/16.
 */

public class Trailers {
    @SerializedName("results")
    ArrayList<Trailer> trailers = new ArrayList<>();

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }
}
