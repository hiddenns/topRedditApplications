package com.topredditapp.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Child{
    @Element(name = "kind")
    public String kind;
    @Element(name = "data")
    public Data data;
}

