package com.vasilev.news.rbk.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;

@Data
@JacksonXmlRootElement
public class RbkFeedItem {

    @JacksonXmlProperty
    private String title;

    @JacksonXmlProperty
    private String description;

    @JacksonXmlProperty(localName = "newsDate_timestamp")
    private Long timestamp;
}
