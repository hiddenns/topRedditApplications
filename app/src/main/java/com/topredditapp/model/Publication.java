package com.topredditapp.model;

import java.io.Serializable;

public class Publication implements Serializable {
    private String id;
    private String title;
    private String ups;
    private String description;
    private String num_comments;
    private String created_utc;
    private Thumbnail thumbnail;

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

    public String getUps() {
        return ups;
    }

    public void setUps(String ups) {
        this.ups = ups;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNum_comments() {
        return num_comments;
    }

    public void setNum_comments(String num_comments) {
        this.num_comments = num_comments;
    }

    public String getCreated_utc() {
        return created_utc;
    }

    public void setCreated_utc(String created_utc) {
        this.created_utc = created_utc;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }
}
