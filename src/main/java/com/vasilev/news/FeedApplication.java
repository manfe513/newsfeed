package com.vasilev.news;

import com.vasilev.news.props.NetworkProperties;
import com.vasilev.news.props.SchedulerProperties;
import com.vasilev.news.props.SourcesProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		NetworkProperties.class,
		SchedulerProperties.class,
		SourcesProperties.class,
})
public class FeedApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedApplication.class, args);
	}
}
