package com.topredditapp;

import com.topredditapp.model.Root;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RedditAPI {
    String BASE_URL = "https://www.reddit.com/";
    @GET("top.json")
    Call<Root> getFeed();

}
