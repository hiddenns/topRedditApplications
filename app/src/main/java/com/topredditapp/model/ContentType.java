package com.topredditapp.model;

public enum ContentType {
    Photo(1), Video(2);

    private int indexType;

    ContentType(int valueType) {
        this.indexType = valueType;
    }

    public int getIndexType() {
        return indexType;
    }

}
