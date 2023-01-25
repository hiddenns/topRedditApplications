package com.topredditapp;

import static com.topredditapp.PaginationListener.PAGE_START;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.topredditapp.controller.Controller;
import com.topredditapp.model.Publication;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";

    private final Controller redditController = new Controller();
    private RedditListAdapter rwAdapter;
    private SwipeRefreshLayout swipeRefresh;

    private RecyclerView recyclerView;
    private Button button;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
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

        final ArrayList<Publication> items = redditController.getPublications();
        new Handler().postDelayed(() -> {
            /**
             * manage progress view
             */
            Log.d("MainActivity:", "handler");
            if (currentPage != PAGE_START) rwAdapter.removeLoading();
            Log.d("MainActivity:", "current page = " + currentPage);
            rwAdapter.addItems(items);
            swipeRefresh.setRefreshing(false);
            // check weather is last page or not
            if (currentPage < totalPage) {
                rwAdapter.addLoading();
            } else {
                Log.d("MainActivity:", "isLastPage");
                isLastPage = true;
            }
            isLoading = false;
        }, 1500);

    }

    private void init() {
        button = findViewById(R.id.button_test);
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
            protected void loadMoreItems() { // problem
                isLoading = true;
                currentPage++;
                Log.d("MainActivity:", "scroll current page: " + currentPage);
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
        doRedditApiCall();
    }

}