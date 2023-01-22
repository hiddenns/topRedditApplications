package com.topredditapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.topredditapp.model.Root;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//key vJre4waKAbNUge9ppLsNzw
public class Controller implements Callback<Root> {
    String BASE_URL = "https://www.reddit.com/top.json/";

    public Root root;

    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.reddit.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RedditAPI redditAPI = retrofit.create(RedditAPI.class);
        Call<Root> call = redditAPI.getFeed();

        call.enqueue(this);
        System.out.println("sd");
    }

    @Override
    public void onResponse(Call<Root> call, Response<Root> response) {
        System.out.println("Response\n");
        if (response.isSuccessful()) {
            root = response.body();
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<Root> call, Throwable t) {
        System.out.println("FAILURE");
    }

}
