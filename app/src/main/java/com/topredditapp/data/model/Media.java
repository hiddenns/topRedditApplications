package com.topredditapp.data.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Media {

    @Element(name = "reddit_video")
    private RedditVideoPreview reddit_video;

    public RedditVideoPreview getReddit_video() {
        return reddit_video;
    }
}
