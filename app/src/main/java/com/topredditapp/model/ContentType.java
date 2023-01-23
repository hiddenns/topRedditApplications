package com.topredditapp.model;

public enum ContentType {
    Photo(0), Video(1), Gif(3);

    private int indexType;

    ContentType(int valueType) {
        this.indexType = valueType;
    }

    public int getIndexType() {
        return indexType;
    }

}
