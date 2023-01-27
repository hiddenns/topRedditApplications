package com.topredditapp.ui.activity;


import static com.topredditapp.utils.Const.BASE_URL;
import static com.topredditapp.utils.Const.PAGE_SIZE;
import static com.topredditapp.ui.PaginationListener.PAGE_START;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.topredditapp.R;
import com.topredditapp.controller.RedditListAdapter;
import com.topredditapp.controller.Controller;
import com.topredditapp.data.model.Child;
import com.topredditapp.data.model.Publication;
import com.topredditapp.data.model.Root;
import com.topredditapp.data.redditapi.RedditAPI;
import com.topredditapp.ui.PaginationListener;

import org.simpleframework.xml.core.Persist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";
    private static final String LIST_STATE_KEY = "LIST_STATE_KEY";

    private final Controller redditController = new Controller();
    private RedditListAdapter rwAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private Parcelable mLayoutManagerState;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    int itemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        fillRecyclerView();
        doRedditApiCall();
        if (savedInstanceState != null) {
            mLayoutManagerState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            recyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerState);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        recyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerState);
//    }

    @Override
    protected void onPause() {
        super.onPause();
        mLayoutManagerState = recyclerView.getLayoutManager().onSaveInstanceState();
         }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_STATE_KEY, mLayoutManagerState);
    }

//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//    }

//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//
//    }

    // start retrofit request
    private void doRedditApiCall() {
        try {
            redditController.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(() -> {
            ArrayList<Publication> items;

            // If there's a need to upload more data. Already existing data can be truncated
            if (currentPage != 1 && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                items = redditController.getPublications()
                        .stream()
                        .skip((long) (currentPage - 1) * PAGE_SIZE)
                        .collect(Collectors.toCollection(ArrayList::new));
            } else {
                items = redditController.getPublications();
            }

            //manage progress view
            if (currentPage != PAGE_START) rwAdapter.removeLoading();
            rwAdapter.addItems(items);
            swipeRefresh.setRefreshing(false);
            rwAdapter.addLoading();
            recyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerState);
            isLoading = false;
            rwAdapter.notifyDataSetChanged();

        }, 1500);
        Log.d(TAG, "doRedditApiCall finish");
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefresh = findViewById(R.id.swipeRefresh);
    }

    private void fillRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rwAdapter = new RedditListAdapter(new ArrayList<>());
        //rwAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        recyclerView.setAdapter(rwAdapter);
        swipeRefresh.setOnRefreshListener(this);

        // fetch to RedditController recycleViewAdapter for notify them
        // when data is loaded
        redditController.setRecyclerView(rwAdapter);

        //add scroll listener while user reach in bottom load more will call
        recyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                Log.d(TAG, "scroll current page: " + currentPage);
                doRedditApiCall();
                //mLayoutManagerState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        rwAdapter.clear();
        redditController.clearData();
        doRedditApiCall();
        Log.d(TAG, "refresh");
    }

//    public void start() {
//        Log.d(TAG, "Start()");
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        RedditAPI redditAPI = retrofit.create(RedditAPI.class);
//        Call<Root> call;
//
//        if (afterDataId == null) {
//            call = redditAPI.getTopContent(PAGE_SIZE);
//            Log.d(TAG, "call request DEFAULT");
//        } else {
//            Log.d(TAG, "call request AFTER id: " + afterDataId);
//            call = redditAPI.getTopContent(PAGE_SIZE, afterDataId);
//        }
//        Log.d(TAG, "call request done!");
//
//        call.enqueue(new Callback<Root>() {
//            @Override
//            public void onResponse(@NonNull Call<Root> call, Response<Root> response) {
//                Log.d(TAG, "onResponse()");
//
//                if (response.isSuccessful() && response.body() != null) {
//                    ArrayList<Child> children = response.body().getData().children;
//
//                    for (int i = 0; i < children.size(); i++) {
//                        Publication publication = new Publication();
//                        publication.setId(children.get(i).getData().id);
//                        publication.setCreated(children.get(i).getData().created);
//                        publication.setThumbnail(children.get(i).getData().thumbnail);
//                        publication.setNumComments(children.get(i).getData().num_comments);
//                        publication.setTitle(children.get(i).getData().title);
//                        publication.setUps(children.get(i).getData().ups);
//                        publication.setAuthor(children.get(i).getData().author);
//                        publication.setMedia(children.get(i).getData().media);
//                        publication.setUrl(children.get(i).getData().url);
//                        publication.setVideo(children.get(i).getData().is_video);
//                        publication.setPostHint(children.get(i).getData().post_hint);
//                        publication.setPreview(children.get(i).getData().preview);
//                        publication.defineContentType();
//                        publications.add(publication);
//                        Log.d(TAG, "add publication title: " + publication.getTitle());
//                    }
//
//                    ArrayList<Publication> items = new ArrayList<>();
//                    // If there's a need to upload more data. Already existing data can be truncated
//                    if (currentPage != 1 && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                        items = publications
//                                .stream()
//                                .skip((long) (currentPage - 1) * PAGE_SIZE)
//                                .collect(Collectors.toCollection(ArrayList::new));
//                    } else {
//                        items = publications;
//                    }
//
//                    publications = items;
//                    Log.d(TAG, "add data ID: " + response.body().getData().after);
//                    if (currentPage != PAGE_START) rwAdapter.removeLoading();
//                    rwAdapter.addItems(publications);
//                    rwAdapter.notifyDataSetChanged();
//                    swipeRefresh.setRefreshing(false);
//                    rwAdapter.addLoading();
//                    recyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerState);
//                    isLoading = false;
//                    afterDataId = response.body().getData().after;
//                    counterPages++;
//
//                    Log.d(TAG, "Counter pages: " + counterPages +
//                            "\n======== end request ==============");
//                } else {
//                    System.err.println(response.errorBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Root> call, Throwable t) {
//                System.err.println(t.getMessage());
//                t.printStackTrace();
//                Log.d(TAG, "Failure request! " + t.getMessage());
//            }
//        });
    }

