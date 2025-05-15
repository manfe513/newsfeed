package com.vasilev.news.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record AddSourceRequest(
        @JsonProperty("name") String name,
        @JsonProperty("url") String url,

        @JsonProperty("rules") AddSourceRequestRules rules
) {}

