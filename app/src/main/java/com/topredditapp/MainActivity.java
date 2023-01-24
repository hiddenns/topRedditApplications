package com.topredditapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.topredditapp.controller.Controller;
import com.topredditapp.model.Publication;

import java.io.IOException;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private final Controller redditController = new Controller();
    private RedditListAdapter rwAdapter;

    private RecyclerView recyclerView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        fillRecyclerView();

        button.setOnClickListener(view -> {
            //Toast.makeText(MainActivity.this, controller.getPublications().get(0).getTitle(), Toast.LENGTH_LONG).show();
        });

        // fetch to RedditController recycleViewAdapter for notify them
        // when data is loaded
        redditController.setRecyclerView(rwAdapter);

        try {
            // start retrofit request
            redditController.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void init() {
        button = findViewById(R.id.button_test);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void fillRecyclerView() {
        rwAdapter = new RedditListAdapter(redditController.getPublications());
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rwAdapter);

    }

//    public class DataLoader extends AsyncTask<Void, Void, Boolean> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressBarDataLoader.setVisibility(View.VISIBLE);
//            Log.d("prog", "onPreExecute");
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//            try {
//                redditController.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aVoid) {
//            super.onPostExecute(aVoid);
//            //fillRecyclerView(redditController.getPublications());
//
//            rwAdapter = new RedditListAdapter(redditController.getPublications());
//            //recyclerView.setHasFixedSize(true);
//            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//            recyclerView.setAdapter(rwAdapter);
//
//
//            progressBarDataLoader.setVisibility(View.INVISIBLE);
//            Log.d("prog", "onPostExecute");
//        }
//
//    }
}