package com.vasilev.news.props;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "scheduler")
public class SchedulerProperties {

    public boolean enabled;
    public String cron;
}