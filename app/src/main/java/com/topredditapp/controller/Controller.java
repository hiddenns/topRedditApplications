package com.topredditapp.controller;


import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.topredditapp.RedditAPI;
import com.topredditapp.RedditListAdapter;
import com.topredditapp.model.Child;
import com.topredditapp.model.Publication;
import com.topredditapp.model.Root;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//key vJre4waKAbNUge9ppLsNzw
public class Controller {
    private final String BASE_URL = "https://www.reddit.com/";

    private ArrayList<Publication> publications = new ArrayList<>();

    public RedditListAdapter recyclerView;

    public void setRecyclerView(RedditListAdapter recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void start() throws IOException {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RedditAPI redditAPI = retrofit.create(RedditAPI.class);
        Call<Root> call = redditAPI.getFeed();

//        call.enqueue(this);
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(@NonNull Call<Root> call, Response<Root> response) {
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

                    recyclerView.notifyDataSetChanged();
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                System.out.println("FAILURE");
            }
        });
//        call.execute();
//        Log.d("prog", "loading done...");
    }

    public ArrayList<Publication> getPublications() {
        return publications;
    }

}
