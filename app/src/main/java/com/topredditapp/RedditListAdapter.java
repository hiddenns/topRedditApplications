package com.topredditapp;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.topredditapp.model.Publication;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RedditListAdapter extends RecyclerView.Adapter<RedditListAdapter.ViewHolder> {
    private List<Publication> publicationList;

    public RedditListAdapter(List<Publication> publicationList) {
        this.publicationList = publicationList;
    }

    @NonNull
    @Override
    public RedditListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RedditListAdapter.ViewHolder holder, int position) {
        final Publication myListData = publicationList.get(position);
        holder.textView.setText(publicationList.get(position).getTitle());
        if (publicationList.get(position).isVideo() || publicationList.get(position).getMedia() == null) {
            URL myUrl = null;
            try {
                myUrl = new URL(publicationList.get(position).getUrl());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            InputStream inputStream = null;

            Drawable drawable = Drawable.createFromStream(inputStream, null);
//            i.setImageDrawable(drawable);
            //holder.imageView.setImageResource(drawable);
            holder.imageView.setImageDrawable(null);
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: " + myListData.getTitle(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return publicationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}
