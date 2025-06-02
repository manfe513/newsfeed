package com.vasilev.news.rbk.model;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;

@Data
@JacksonXmlRootElement
public class RbkFeed {

    @JacksonXmlProperty
    private RbkFeedChannel channel;

    public List<RbkFeedItem> getItems() {
        return channel.getItems();
    }
}

