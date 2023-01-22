package com.topredditapp;

import android.os.AsyncTask;

import com.topredditapp.controller.Controller;
import com.topredditapp.model.Publication;

import java.io.IOException;
import java.util.List;

public class DataLoader extends AsyncTask<Void, Void, List<Publication>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Publication> doInBackground(Void... voids) {
//        Controller controller = new Controller();
//        try {
//            controller.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return controller.getPublications();
        return null;
    }

    @Override
    protected void onPostExecute(List<Publication> publications) {
        super.onPostExecute(publications);
    }

}
