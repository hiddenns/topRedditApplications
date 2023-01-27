package com.topredditapp.data.model;

import org.simpleframework.xml.Root;

@Root(strict = false)
public class RedditVideoPreview {
    public int bitrate_kbps;
    public String fallback_url;
    public int height;
    public int width;
    public String scrubber_media_url;
    public String dash_url;
    public int duration;
    public String hls_url;
    public boolean is_gif;
    public String transcoding_status;
}
