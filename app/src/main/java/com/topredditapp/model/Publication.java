package com.topredditapp.model;

import java.io.Serializable;

public class Publication implements Serializable {
    private String id;
    private String title;
    private String author;
    private int ups;
    private String description;
    private int num_comments;
    private double created_utc;
    private String thumbnail;
    private String url;
    private Media media;
    private boolean isVideo;

    public Publication() {
    }
    public Publication(String id, String title, String author, int ups, String description, int num_comments, double created_utc, String thumbnail, String url, Media media, boolean isVideo) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.ups = ups;
        this.description = description;
        this.num_comments = num_comments;
        this.created_utc = created_utc;
        this.thumbnail = thumbnail;
        this.url = url;
        this.media = media;
        this.isVideo = isVideo;
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

    public int getNum_comments() {
        return num_comments;
    }

    public void setNum_comments(int num_comments) {
        this.num_comments = num_comments;
    }

    public double getCreated_utc() {
        return created_utc;
    }

    public void setCreated_utc(double created_utc) {
        this.created_utc = created_utc;
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
}
