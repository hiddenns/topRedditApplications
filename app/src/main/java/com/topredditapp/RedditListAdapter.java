package com.topredditapp;

import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.topredditapp.model.Publication;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RedditListAdapter extends RecyclerView.Adapter<RedditListAdapter.ViewHolder> {
    private List<Publication> publicationList;

    public RedditListAdapter(List<Publication> publicationList) {
        this.publicationList = publicationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 0:
                View viewItemPhoto = layoutInflater.inflate(R.layout.list_item_photo, parent, false);
                return new ViewHolderImg(viewItemPhoto);
            case 1:
                View viewItemVideo = layoutInflater.inflate(R.layout.list_item_video, parent, false);
                return new ViewHolderVideo(viewItemVideo);
            default:
                View viewItem = layoutInflater.inflate(R.layout.list_item, parent, false);
                return new ViewHolder(viewItem);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return publicationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return publicationList.get(position).getContentType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textAuthor;
        public TextView textCreatedAt;
        public TextView textTitle;
        public TextView textUps;
        public TextView textComments;
        public ImageView imageProfile;
        public ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageProfile = itemView.findViewById(R.id.profileImage);
            this.textAuthor = itemView.findViewById(R.id.authorText);
            this.textCreatedAt = itemView.findViewById(R.id.createdPublText);
            this.textTitle = itemView.findViewById(R.id.titleText);
            this.textUps = itemView.findViewById(R.id.upsText);
            this.constraintLayout = itemView.findViewById(R.id.constraintContentLayout);
            this.textComments = itemView.findViewById(R.id.numCommentsText);
        }

        public void onBind(int position) {
            Publication data = publicationList.get(position);
            textComments.setText(data.getNumComments() + " comments");
            textAuthor.setText(data.getAuthor());
            textTitle.setText(data.getTitle());
            textUps.setText(((double) (data.getUps() / 100) / 10) + "k");

            // calculate how many hours ago a publication was posted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Instant createdAt = Instant.ofEpochSecond((long) data.getCreated());
                long hoursDiff = ChronoUnit.HOURS.between(createdAt, Instant.now());
                textCreatedAt.setText(hoursDiff + " hours ago");
            }

            constraintLayout.setOnClickListener(view ->
                    Toast.makeText(view.getContext(), "click on item: " + data.getTitle(), Toast.LENGTH_LONG).show());
        }

    }

    public class ViewHolderImg extends ViewHolder {
        private ImageView imageContent;

        public ViewHolderImg(View itemView) {
            super(itemView);
            this.imageContent = itemView.findViewById(R.id.imageContent);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            Publication data = publicationList.get(position);
            Picasso.get().load(data.getUrl()).into(imageContent);
        }
    }

    public class ViewHolderVideo extends ViewHolder {
        private VideoView videoContent;

        public ViewHolderVideo(View itemView) {
            super(itemView);
            videoContent = itemView.findViewById(R.id.videoContent);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            Publication data = publicationList.get(position);
            Uri uri = Uri.parse(data.getMedia().getReddit_video().fallback_url);
            videoContent.setVideoURI(uri);
            videoContent.start();
        }
    }
}



