package com.topredditapp.data.redditapi;

import com.topredditapp.data.model.Root;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RedditAPI {
    @GET("top.json")
    Call<Root> getTopContent(@Query("limit") int limit);

    @GET("top.json")
    Call<Root> getTopContent(@Query("limit") int limit, @Query("after") String after);

}
