package com.sanath.movies.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sanathnandasiri on 8/2/16.
 */

public class Reviews {
    @SerializedName("results")
    ArrayList<Review> reviews = new ArrayList<>();

    public ArrayList<Review> getReviews() {
        return reviews;
    }
}
