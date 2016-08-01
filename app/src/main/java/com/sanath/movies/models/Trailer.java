package com.sanath.movies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sanathnandasiri on 7/28/16.
 */

public class Trailer implements Parcelable {
    @SerializedName("id")
    public String id;
    @SerializedName("key")
    public String key;
    @SerializedName("name")
    public String name;
    @SerializedName("site")
    public String site;
    @SerializedName("size")
    public String size;

    public String getThumbnailUrl() {
        return "http://img.youtube.com/vi/" + key + "/0.jpg";
    }

    public String getVideoUrl() {
        return "http://www.youtube.com/watch?v=" + key;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeString(this.size);
    }

    public Trailer() {
    }

    protected Trailer(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readString();
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public String toString() {
        return "Trailer{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", site='" + site + '\'' +
                ", size='" + size + '\'' +
                ", thumbnail='" + getThumbnailUrl() + '\'' +
                ", video='" + getVideoUrl() + '\'' +
                '}';
    }
}
