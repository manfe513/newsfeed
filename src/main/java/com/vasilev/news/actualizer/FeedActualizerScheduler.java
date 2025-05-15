package com.vasilev.news.actualizer;

import com.vasilev.news.repository.SourcesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedActualizerScheduler {

    private final FeedLoader feedLoader;
    private final SourcesRepository sourcesRepo;

    @Scheduled(cron = "${scheduler.cron}")
    @SchedulerLock(name = "schedlock.actualizer", lockAtMostFor = "PT30S")
    public void run() {
        log.info(">> FeedActualizerScheduler - start, thread {}", Thread.currentThread().getName());

        LockAssert.assertLocked();

        sourcesRepo.findAll().forEach(feedLoader::load);

        log.info(">> FeedActualizerScheduler - end");
    }
}
