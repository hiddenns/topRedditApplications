package com.topredditapp;

import android.app.Application;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.topredditapp.model.Child;
import com.topredditapp.model.Data;
import com.topredditapp.model.Media;
import com.topredditapp.model.Publication;
import com.topredditapp.model.Root;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//key vJre4waKAbNUge9ppLsNzw
public class Controller implements Callback<Root> {
    String BASE_URL = "https://www.reddit.com/top.json/";

    public Root root;
    ArrayList<Publication> publications = new ArrayList<>();

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
        if (response.isSuccessful() && response.body() != null) {
            ArrayList<Child> children = response.body().getData().children;

            for (int i = 0; i < children.size(); i++) {
                Publication publication = new Publication();
                publication.setId(children.get(i).getData().id);
                publication.setCreated_utc(children.get(i).getData().created_utc);
                publication.setThumbnail(children.get(i).getData().thumbnail);
                publication.setNum_comments(children.get(i).getData().num_comments);
                publication.setTitle(children.get(i).getData().title);
                publication.setUps(children.get(i).getData().ups);
                publication.setAuthor(children.get(i).getData().author);
                publication.setMedia(children.get(i).getData().media);
                publication.setUrl(children.get(i).getData().url);
                publication.setVideo(children.get(i).getData().is_video);

                publications.add(publication);
            }

            System.out.println();
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<Root> call, Throwable t) {
        System.out.println("FAILURE");
    }

}
