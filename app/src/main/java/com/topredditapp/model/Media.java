package com.topredditapp.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Media {

    @Element(name = "reddit_video")
    private RedditVideo reddit_video;

    public RedditVideo getReddit_video() {
        return reddit_video;
    }
}
