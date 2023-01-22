package com.topredditapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.topredditapp.controller.Controller;
import com.topredditapp.model.Publication;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Controller controller = new Controller();
        controller.start();

        Button button = findViewById(R.id.button_test);
        button.setOnClickListener(view -> {
                Toast.makeText(MainActivity.this, controller.getPublications().get(0).getTitle(), Toast.LENGTH_LONG).show();
                fillRecyclerView(controller.getPublications());
        });


    }

    private void fillRecyclerView(List<Publication> publications) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RedditListAdapter adapter = new RedditListAdapter(publications);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}