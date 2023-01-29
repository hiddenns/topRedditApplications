package com.topredditapp.ui.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ProgressBar;

import com.topredditapp.R;
import com.topredditapp.controller.PaginationAdapter;
import com.topredditapp.data.model.Child;
import com.topredditapp.data.model.Publication;
import com.topredditapp.data.model.Root;
import com.topredditapp.data.redditapi.ClientAPI;
import com.topredditapp.data.redditapi.RedditAPI;
import com.topredditapp.ui.PaginationListener;
import com.topredditapp.utils.Const;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "mainActivityLog";
    private static final String LIST_STATE_KEY = "LIST_STATE_KEY";


    private RedditAPI redditAPI;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private PaginationAdapter paginationAdapter = new PaginationAdapter();

    private int itemCount = 0;
    private String afterDataId = null;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;
    private static final int PAGE_START = 1;

    private Parcelable mLayoutManagerState;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayoutManager linearLayoutManager;
    private PaginationListener paginationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        paginationListener = new PaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mLayoutManagerState = recyclerView.getLayoutManager().onSaveInstanceState();
                }
            }
        };

        recyclerView.addOnScrollListener(paginationListener);

//        if (savedInstanceState != null) {
//            mLayoutManagerState = savedInstanceState.getParcelable(LIST_STATE_KEY);
//            recyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerState);
//        }
        loadNextPage();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        paginationAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.ALLOW);
        swipeRefresh.setOnRefreshListener(this);

        redditAPI = ClientAPI.getClient().create(RedditAPI.class);
        linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(paginationAdapter);
    }

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

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mLayoutManagerState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        recyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerState);
    }

    private void loadNextPage() {
        ArrayList<Publication> publications = new ArrayList<>();
        redditAPI.getTopContent(Const.PAGE_SIZE, afterDataId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(@NonNull Call<Root> call, Response<Root> response) {
                if (currentPage != PAGE_START)
                    paginationAdapter.removeLoading();
                isLoading = false;
                ArrayList<Child> children;

                if (response.isSuccessful() && response.body() != null) {
                    children = response.body().getData().children;

                    for (int i = 0; i < children.size(); i++) {
                        Publication publication = extractPublication(children, i);
                        publications.add(publication);
                    }

                    afterDataId = response.body().getData().after;
                    itemCount++;
                    paginationAdapter.addAll(publications);
                    paginationAdapter.addLoading();

                    recyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerState);
                } else {
                    System.err.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                System.err.println(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        paginationAdapter.clear();
        swipeRefresh.setRefreshing(false);
        afterDataId = null;
        loadNextPage();
    }

    @NonNull
    private Publication extractPublication(ArrayList<Child> children, int i) {
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
        return publication;
    }

}

