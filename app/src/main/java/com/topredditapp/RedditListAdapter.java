package com.topredditapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RedditListAdapter extends RecyclerView.Adapter<RedditListAdapter.ViewHolder> {
    private final List<Publication> publicationList;
    private boolean isLoaderVisible = false;

    private static final int VIEW_TYPE_LOADING = 0;
    private static final String TAG = "adapter";

    public RedditListAdapter(List<Publication> publicationList) {
        this.publicationList = publicationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 0:
                Log.d(TAG, "on progressBar holder vt:" + viewType);
                return new ProgressHolder(
                        layoutInflater.inflate(R.layout.item_loading_recycler_view, parent, false));
            case 1:
                Log.d(TAG, "on img holder vt:" + viewType);
                View viewItemPhoto = layoutInflater.inflate(R.layout.list_item_photo, parent, false);
                return new ViewHolderImg(viewItemPhoto);
            case 2:
            case 4:
                Log.d(TAG, "on video holder vt:" + viewType);
                View viewItemVideo = layoutInflater.inflate(R.layout.list_item_video, parent, false);
                return new ViewHolderVideo(viewItemVideo);
            case 3:
                Log.d(TAG, "on video link vt:" + viewType);
                View viewItemLink = layoutInflater.inflate(R.layout.list_item_link, parent, false);
                return new LinkViewHolder(viewItemLink);
            default:
                Log.d(TAG, "on Default holder vt:" + viewType);
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
        return publicationList == null ? 0 : publicationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int itemContentType = publicationList.get(position).getContentType();
        if (isLoaderVisible) {
            return position == publicationList.size() - 1 ? VIEW_TYPE_LOADING : itemContentType;
        }
        return itemContentType;
    }

    public void addItems(ArrayList<Publication> publications) {
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
            if (data.getContentType() == VIEW_TYPE_LOADING) {
                Log.d(TAG,"content type VIEW_TYPE_LOADING");
                return;
            }

            if (data.getId() != null) {
                Log.d(TAG, "onBind position:" + position + "; data id = " + data.getId());
                if (data.getId().equals("10lodta")) {
                    System.out.println();
                }
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
                        Toast.makeText(view.getContext(), "click on item: "
                                + data.getTitle(), Toast.LENGTH_LONG).show());
            }
        }
    }

    public class ViewHolderImg extends ViewHolder {
        @BindView(R.id.imageContent)
        ImageView imageContent;

        public ViewHolderImg(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
        @BindView(R.id.progressBarVideo)
        ProgressBar progressBar;

        public ViewHolderVideo(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            progressBar.setVisibility(View.VISIBLE);
            Publication data = publicationList.get(position);
            String urlPublication;
            if (data.getContentType() == ContentType.Video.getIndexType()) {
                urlPublication = data.getMedia().getReddit_video().fallback_url;
            } else if (data.getContentType() == ContentType.Gif.getIndexType()
                    && data.getPreview().reddit_video_preview != null) {
                urlPublication = data.getPreview().reddit_video_preview.fallback_url;
            } else {
                urlPublication = data.getThumbnail();
            }
            Uri uri = Uri.parse(urlPublication);
            videoContent.setVideoURI(uri);

            videoContent.setOnPreparedListener(mediaPlayer -> {
                progressBar.setVisibility(VideoView.INVISIBLE);
                videoContent.start();
            });
        }
    }

    public class ProgressHolder extends ViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class LinkViewHolder extends ViewHolder {
        @BindView(R.id.thumbnailImg)
        ImageView imageThumbnail;

        LinkViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            Publication data = publicationList.get(position);
            if (!data.getThumbnail().equals("default")) {
                Picasso.get().load(data.getThumbnail()).into(imageThumbnail);
            }
        }
    }
}



