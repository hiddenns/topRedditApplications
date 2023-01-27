package com.topredditapp.model;

public enum ContentType {
    Photo(1), Video(2), Link(3), Gif(4), Default(6);

    private int indexType;

    ContentType(int valueType) {
        this.indexType = valueType;
    }

    public int getIndexType() {
        return indexType;
    }

}
