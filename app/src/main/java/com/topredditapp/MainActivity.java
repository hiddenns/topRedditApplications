package com.topredditapp;


import static com.topredditapp.Const.PAGE_SIZE;
import static com.topredditapp.PaginationListener.PAGE_START;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.topredditapp.controller.Controller;
import com.topredditapp.model.Publication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";

    private final Controller redditController = new Controller();
    private RedditListAdapter rwAdapter;
    private SwipeRefreshLayout swipeRefresh;

    private RecyclerView recyclerView;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    int itemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        fillRecyclerView();
        doRedditApiCall();
    }

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
            /**
             * manage progress view
             */
            if (currentPage != PAGE_START) rwAdapter.removeLoading();
            rwAdapter.addItems(items);
            swipeRefresh.setRefreshing(false);
            rwAdapter.addLoading();
            isLoading = false;
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rwAdapter = new RedditListAdapter(new ArrayList<>());
        recyclerView.setAdapter(rwAdapter);

        swipeRefresh.setOnRefreshListener(this);

        // fetch to RedditController recycleViewAdapter for notify them
        // when data is loaded
        redditController.setRecyclerView(rwAdapter);

        /**
         * add scroll listener while user reach in bottom load more will call
         */
        recyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                Log.d(TAG, "scroll current page: " + currentPage);
                doRedditApiCall();
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

}