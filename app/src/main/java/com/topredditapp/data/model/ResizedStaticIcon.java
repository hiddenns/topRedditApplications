package com.topredditapp.data.model;

import org.simpleframework.xml.Root;

@Root(strict = false)
public class ResizedStaticIcon {
    public String url;
    public int width;
    public int height;
}