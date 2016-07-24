package com.sanath.movies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sanathnandasiri on 7/14/16.
 *
 */

public class Movie implements Parcelable {
    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public String title;
    @SerializedName("original_title")
    public String originalTitle;
    @SerializedName("original_language")
    public String originalLanguage;
    @SerializedName("release_date")
    public String releaseDate;
    @SerializedName("overview")
    public String overview;
    @SerializedName("poster_path")
    public String posterPath;
    @SerializedName("backdrop_path")
    public String backdropPath;
    @SerializedName("popularity")
    public String popularity;
    @SerializedName("vote_count")
    public String voteCount;
    @SerializedName("vote_average")
    public String voteAverage;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.originalTitle);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.releaseDate);
        dest.writeString(this.overview);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
        dest.writeString(this.popularity);
        dest.writeString(this.voteCount);
        dest.writeString(this.voteAverage);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.originalTitle = in.readString();
        this.originalLanguage = in.readString();
        this.releaseDate = in.readString();
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.popularity = in.readString();
        this.voteCount = in.readString();
        this.voteAverage = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", overview='" + overview + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", popularity='" + popularity + '\'' +
                ", voteCount='" + voteCount + '\'' +
                ", voteAverage='" + voteAverage + '\'' +
                '}';
    }
}
