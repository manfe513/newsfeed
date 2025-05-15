package com.vasilev.news.actualizer;

import com.vasilev.news.model.db.FeedEntity;
import com.vasilev.news.model.db.SourceEntity;
import com.vasilev.news.parser.FeedParser;
import com.vasilev.news.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedLoader {

    private final FeedClient feedClient;

    private final FeedParser parser;

    private final FeedRepository feedRepository;

    /**
     * @param source - по какому источнику загрузить новости
     */
    public void load(SourceEntity source) {
        log.info(">> FeedLoader: start fetching, source = {}", source.getName());

        // 0 - получили новости
        List<FeedEntity> itemsFromSource = parser.parse(
                source.getName(),
                feedClient.getFeed(source).orElse(""),
                source.getRules()
        );

        // 1 - выбрали свежие
        List<FeedEntity> itemsToSave = filterItemsToSave(source, itemsFromSource);

        // 2 - сохранили
        List<FeedEntity> savedItems = feedRepository.saveAll(itemsToSave);
        log.info(">> saved items count = {}", savedItems.size());
    }

    private List<FeedEntity> filterItemsToSave(
            SourceEntity source,
            List<FeedEntity> itemsFromSource
    ) {
        Optional<FeedEntity> lastSavedItem = feedRepository.findFirstBySourceOrderByDateDesc(source.getName());
        if (lastSavedItem.isEmpty()) {
            return itemsFromSource;
        }

        val lastSavedDate = lastSavedItem.get().getDate();

        return itemsFromSource.stream()
                .filter(item -> item.getDate().isAfter(lastSavedDate))
                .toList();
    }
}
