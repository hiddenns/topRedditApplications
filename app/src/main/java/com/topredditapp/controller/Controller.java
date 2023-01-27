package com.topredditapp.controller;


import static com.topredditapp.Const.BASE_URL;
import static com.topredditapp.Const.PAGE_SIZE;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.topredditapp.MainActivity;
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

public class Controller {
    private final String TAG = "Controller";
    private String afterDataId = null;
    private int counterPages = 0;

    private ArrayList<Publication> publications = new ArrayList<>();

    public RedditListAdapter recyclerView;

    public void setRecyclerView(RedditListAdapter recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void start() throws IOException {
        Log.d(TAG, "Start()");
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RedditAPI redditAPI = retrofit.create(RedditAPI.class);
        Call<Root> call;

        if (afterDataId == null) {
            call = redditAPI.getTopContent(PAGE_SIZE);
            Log.d(TAG, "call request DEFAULT");
        } else {
            Log.d(TAG, "call request AFTER id: " + afterDataId);
            call = redditAPI.getTopContent(PAGE_SIZE, afterDataId);
        }
        Log.d(TAG, "call request done!");

        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(@NonNull Call<Root> call, Response<Root> response) {
                Log.d(TAG, "onResponse()");

                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Child> children = response.body().getData().children;

                    for (int i = 0; i < children.size(); i++) {
                        Publication publication = new Publication();
                        publication.setId(children.get(i).getData().id);
                        publication.setCreated(children.get(i).getData().created);
                        publication.setThumbnail(children.get(i).getData().thumbnail);
                        publication.setNumComments(children.get(i).getData().num_comments);
                        publication.setTitle(children.get(i).getData().title);
                        publication.setUps(children.get(i).getData().ups);
                        publication.setAuthor(children.get(i).getData().author);
                        publication.setMedia(children.get(i).getData().media);
                        publication.setUrl(children.get(i).getData().url);
                        publication.setVideo(children.get(i).getData().is_video);
                        publication.setPostHint(children.get(i).getData().post_hint);
                        publication.setPreview(children.get(i).getData().preview);
                        publication.defineContentType();
                        publications.add(publication);
                        Log.d(TAG, "add publication title: " + publication.getTitle());
                    }
                    Log.d(TAG, "add data ID: " + response.body().getData().after);
                    afterDataId = response.body().getData().after;
                    recyclerView.notifyDataSetChanged();
                    counterPages++;

                    Log.d(TAG, "Counter pages: " + counterPages +
                            "\n======== end request ==============");
                } else {
                    System.err.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                System.err.println(t.getMessage());
                t.printStackTrace();
                Log.d(TAG, "Failure request! " + t.getMessage());
            }
        });
    }

    public ArrayList<Publication> getPublications() {
        return publications;
    }

    public void clearData() {
        Log.d(TAG, "clear data");
        afterDataId = null;
        publications.clear();
        counterPages = 0;
        Log.d(TAG, "refresh");
    }

}
