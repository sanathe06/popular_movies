package com.sanath.movies;

import com.sanath.movies.models.Api;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Sanath Nandasiri on 7/17/2016.
 */
public class ApiTest {
    @Test
    public void getPopularMovies() throws Exception {
        Assert.assertTrue(Api.getPopularMovies().size() > 0);
    }

    @Test
    public void getTopRatedMovies() throws Exception {

    }

    @Test
    public void getMovies() throws Exception {

    }

    @Test
    public void get() throws Exception {

    }

}