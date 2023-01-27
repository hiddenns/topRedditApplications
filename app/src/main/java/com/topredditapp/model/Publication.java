package com.topredditapp.model;

import android.util.Log;

import java.io.Serializable;
import java.util.Objects;

public class Publication implements Serializable {
    private final String TAG = "Publication";
    private String id;
    private String title;
    private String author;
    private int ups;
    private String description;
    private int numComments;
    private double created;
    private String thumbnail;
    private String url;
    private Media media;
    private boolean isVideo;
    private ContentType contentType = ContentType.Default;
    private String iconUrl;
    private String postHint;
    private Preview preview;

    public Publication() {
    }

    public void defineContentType() {
        if (postHint == null && !isVideo || Objects.equals(postHint, "link")) {
            this.contentType = ContentType.Link;
        } else if (Objects.equals(postHint, "rich:video")) {
            this.contentType = ContentType.Gif;
        } else if (isVideo) {
            this.contentType = ContentType.Video;
        } else if (Objects.equals(postHint, "image")) {
            this.contentType = ContentType.Photo;
        } else {
            this.contentType = ContentType.Link;
        }
    }

    public Preview getPreview() {
        return preview;
    }

    public void setPreview(Preview preview) {
        this.preview = preview;
    }

    public String getPostHint() {
        return postHint;
    }

    public void setPostHint(String postHint) {
        this.postHint = postHint;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getContentType() {
//        defineContentType();
        return contentType.getIndexType();
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUps() {
        return ups;
    }

    public void setUps(int ups) {
        this.ups = ups;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public double getCreated() {
        return created;
    }

    public void setCreated(double created) {
        this.created = created;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publication that = (Publication) o;
        if (that.getId() == id) return true;
        return ups == that.ups && numComments == that.numComments && Double.compare(that.created, created) == 0 && isVideo == that.isVideo && Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(author, that.author) && Objects.equals(description, that.description) && Objects.equals(thumbnail, that.thumbnail) && Objects.equals(url, that.url) && Objects.equals(media, that.media) && contentType == that.contentType && Objects.equals(iconUrl, that.iconUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
