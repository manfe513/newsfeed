package com.vasilev.news.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record RemoveSourceRequest(
        @JsonProperty("id") Integer id
) {}

