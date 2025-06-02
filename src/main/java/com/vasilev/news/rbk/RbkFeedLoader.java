package com.vasilev.news.rbk;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.vasilev.news.model.db.FeedEntity;
import com.vasilev.news.rbk.model.RbkFeedItem;
import com.vasilev.news.repository.FeedRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@Component
@RequiredArgsConstructor
public class RbkFeedLoader {

    private final RbkFeedClient rbkFeedClient;
    private final FeedRepository feedRepository;

    /**
     * @param pageSizeToLoad - сколько новостей загрузить
     * @return - кол-во сохранённых новостей
     */
    public int load(int pageSizeToLoad) {
        log.info(">> start fetching, pageSize = {}", pageSizeToLoad);

        // 0
        List<FeedEntity> itemsFromRbk = map(rbkFeedClient.getFeed(pageSizeToLoad));

        // 1
        List<FeedEntity> itemsToSave = filterItemsToSave(itemsFromRbk);

        // 2
        List<FeedEntity> savedItems = feedRepository.saveAll(itemsToSave);
        log.info(">> saved items count = {}", savedItems.size());

        return savedItems.size();
    }

    private List<FeedEntity> filterItemsToSave(List<FeedEntity> itemsFromSource) {
        Optional<FeedEntity> lastSavedItem = feedRepository.findFirstBySourceOrderByDateDesc(RbkConst.SOURCE_NAME);
        if (lastSavedItem.isEmpty()) {
            return itemsFromSource;
        }

        val lastSavedDate = lastSavedItem.get().getDate();

        return itemsFromSource.stream()
                .filter(item -> item.getDate().isAfter(lastSavedDate))
                .toList();
    }

    private List<FeedEntity> map(List<RbkFeedItem> items) {
        log.info(">> MAPPING RBK FEED ITEMS:");

        if (items.isEmpty()) {
            log.info("\t no items");
            return List.of();
        }

        log.info("{}", items);

        return items.stream()
                .map(this::toFeedItem)
                .toList();
    }

    private FeedEntity toFeedItem(RbkFeedItem rbkFeedItem) {
        return new FeedEntity(
                Instant.ofEpochMilli(rbkFeedItem.getTimestamp() * 1000),
                RbkConst.SOURCE_NAME,
                rbkFeedItem.getTitle(),
                rbkFeedItem.getDescription()
        );
    }
}
