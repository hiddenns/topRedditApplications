package com.topredditapp.data.model;

import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(strict = false)
public class AllAwardings {
    public Object giver_coin_reward;
    public Object subreddit_id;
    public boolean is_new;
    public Object days_of_drip_extension;
    public int coin_price;
    public String id;
    public Object penny_donate;
    public String award_sub_type;
    public int coin_reward;
    public String icon_url;
    public Object days_of_premium;
    public Object tiers_by_required_awardings;
    public ArrayList<ResizedStaticIcon> resized_icons;
    public int icon_width;
    public int static_icon_width;
    public Object start_date;
    public boolean is_enabled;
    public Object awardings_required_to_grant_benefits;
    public String description;
    public Object end_date;
    public Object sticky_duration_seconds;
    public int subreddit_coin_reward;
    public int count;
    public int static_icon_height;
    public String name;
    public ArrayList<ResizedStaticIcon> resized_static_icons;
    public Object icon_format;
    public int icon_height;
    public Object penny_price;
    public String award_type;
    public String static_icon_url;
}
