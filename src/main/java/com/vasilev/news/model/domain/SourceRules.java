package com.vasilev.news.model.domain;

import lombok.Builder;

@Builder
public record SourceRules (

    Integer id,
    String itemPath,
    String titlePath,
    String descriptionPath,
    String datePath,
    String dateFormat
) {}
