package com.topredditapp;

import android.net.Uri;
import android.os.Build;
import android.util.Log;
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
import com.topredditapp.model.ContentType;
import com.topredditapp.model.Publication;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RedditListAdapter extends RecyclerView.Adapter<RedditListAdapter.ViewHolder> {
    private List<Publication> publicationList;
    private boolean isLoaderVisible = false;

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    public RedditListAdapter(List<Publication> publicationList) {
        this.publicationList = publicationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        Log.d("adapter", "onCreate ViewHolder: " + viewType);
//        if (viewType == VIEW_TYPE_LOADING) {
//            return new ProgressHolder(
//                    layoutInflater.inflate(R.layout.item_loading, parent, false));
//        }

        switch (viewType) {
            case 0:
                Log.d("adapter", "on progressBar holder vt:" + viewType);
                return new ProgressHolder(
                        layoutInflater.inflate(R.layout.item_loading, parent, false));
            case 1:
                Log.d("adapter", "on img holder vt:" + viewType);
                View viewItemPhoto = layoutInflater.inflate(R.layout.list_item_photo, parent, false);
                return new ViewHolderImg(viewItemPhoto);
            case 2:
                Log.d("adapter", "on video holder vt:" + viewType);
                View viewItemVideo = layoutInflater.inflate(R.layout.list_item_video, parent, false);
                return new ViewHolderVideo(viewItemVideo);
            default:
                Log.d("adapter", "on Default holder vt:" + viewType);
                View viewItem = layoutInflater.inflate(R.layout.list_item, parent, false);
                return new ViewHolder(viewItem);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("adapter: ", "onBindViewHolder: " + position);
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return publicationList == null ? 0 : publicationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int itemContentType = publicationList.get(position).getContentType();
        if (isLoaderVisible) {
            return position == publicationList.size() - 1 ? VIEW_TYPE_LOADING : itemContentType;
        } else {
            return itemContentType;
        }
    }

    public void addItems(List<Publication> publications) {
        publicationList.addAll(publications);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        publicationList.add(new Publication());
        notifyItemInserted(publicationList.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = publicationList.size() - 1;
        Publication item = getItem(position);
        if (item != null) {
            publicationList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        publicationList.clear();
        notifyDataSetChanged();
    }

    Publication getItem(int position) {
        return publicationList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textAuthor;
        public TextView textCreatedAt;
        public TextView textTitle;
        public TextView textUps;
        public TextView textComments;
        public ImageView imageProfile;
        public ConstraintLayout constraintLayout;

        private int mCurrentPosition;

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
            mCurrentPosition = position;
            Publication data = publicationList.get(position);
            if (data.getContentType() == VIEW_TYPE_LOADING) {
                Log.d("adapter","content type VIEW_TYPE_LOADING");
                return;
            }

            if (data.getId() != null) {
                Log.d("adapter", "onBind position:" + position + "; data id = " + data.getId());
                textComments.setText(data.getNumComments() + " comments");
                textAuthor.setText(data.getAuthor());
                textTitle.setText(data.getTitle());
                textUps.setText((double) (data.getUps() / 100) / 10 + "k");

                // calculate how many hours ago a publication was posted
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Instant createdAt = Instant.ofEpochSecond((long) data.getCreated());
                    long hoursDiff = ChronoUnit.HOURS.between(createdAt, Instant.now());
                    textCreatedAt.setText(hoursDiff + " hours ago");
                }

                constraintLayout.setOnClickListener(view ->
                        Toast.makeText(view.getContext(), "click on item: " + data.getTitle(), Toast.LENGTH_LONG).show());
                //clear();
            }
        }

        public int getCurrentPosition() {
            return mCurrentPosition;
        }

    }

    public class ViewHolderImg extends ViewHolder {
        @BindView(R.id.imageContent)
        ImageView imageContent;

        public ViewHolderImg(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            this.imageContent = itemView.findViewById(R.id.imageContent);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            Publication data = publicationList.get(position);
            Picasso.get().load(data.getUrl()).into(imageContent);
        }


    }

    public class ViewHolderVideo extends ViewHolder {
        @BindView(R.id.videoContent)
        VideoView videoContent;

        public ViewHolderVideo(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            videoContent = itemView.findViewById(R.id.videoContent);
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

    public class ProgressHolder extends ViewHolder {

        ProgressHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
        }



    }
}



