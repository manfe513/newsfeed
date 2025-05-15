package com.vasilev.news.model.domain;

import lombok.Builder;

@Builder
public record Source(
        Integer id,
        String name,
        String url,
        SourceRules rules
) {}
