package com.example.android.moviesremake.retrofit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PepovPC on 1/16/2018.
 */

public class MovieRetrofit implements Parcelable {

    @SerializedName("poster_path")
    private String image;

    @SerializedName("id")
    private int id;

    @SerializedName("original_title")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("vote_average")
    private String vote;

    @SerializedName("release_date")
    private String release;


    public MovieRetrofit(String image,int id, String title, String overview, String vote, String release) {
        this.image = image;
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.vote = vote;
        this.release = release;
    }

    public String getImage() {
        return "http://image.tmdb.org/t/p/w185/" + image;
    }

    public void setImage(String image) {
        this.image = "http://image.tmdb.org/t/p/w185/" + image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getVote() {
        return vote;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getRelease() {
        return release;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(vote);
        dest.writeString(release);
    }


    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public MovieRetrofit createFromParcel(Parcel in) {
            return new MovieRetrofit(in);
        }

        public MovieRetrofit[] newArray(int size) {
            return new MovieRetrofit[size];
        }
    };

    public MovieRetrofit(Parcel in) {
        image = in.readString();
        id = in.readInt();
        title = in.readString();
        overview = in.readString();
        vote = in.readString();
        release = in.readString();
    }
}
