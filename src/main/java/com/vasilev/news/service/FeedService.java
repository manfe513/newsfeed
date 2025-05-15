package com.vasilev.news.service;

import com.vasilev.news.actualizer.FeedActualizer;
import com.vasilev.news.repository.FeedRepository;
import com.vasilev.news.model.db.FeedEntity;
import com.vasilev.news.model.domain.FeedListItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {

    private final Set<FeedActualizer> actualizers;
    private final FeedRepository feedRepository;

    @Transactional(propagation = Propagation.NEVER)
    public void actualizeBySource(String source) {
        Optional<FeedActualizer> actualizer = actualizers.stream()
                .filter(a -> a.sourceName().equals(source))
                .findFirst();

        if (actualizer.isEmpty()) {
            throw new IllegalArgumentException("Unknown source: %s".formatted(source));
        }

        actualizer.get().actualize();
    }

    @Transactional(readOnly = true)
    public List<FeedListItem> getFeed(
            Instant dateFrom,
            int count,
            String searchText
    ) {
        List<FeedEntity> feedEntities;

        if (searchText != null && !searchText.isBlank()) {
            feedEntities = feedRepository.findAllBySearch(
                    dateFrom,
                    searchText,
                    Limit.of(count)
            );
        } else {
            feedEntities = feedRepository.findAllByDateBeforeOrderByDateDesc(
                    dateFrom,
                    Limit.of(count)
            );
        }

        return toFeedListItems(feedEntities);
    }

    private List<FeedListItem> toFeedListItems(List<FeedEntity> feedEntities) {
        return feedEntities.stream().map(entity -> new FeedListItem(
                entity.getId(),
                entity.getDate().toString(),
                entity.getSource(),
                entity.getTitle(),
                entity.getDescription()
        )).toList();
    }
}
