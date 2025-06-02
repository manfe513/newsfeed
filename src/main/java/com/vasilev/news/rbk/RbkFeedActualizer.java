package com.vasilev.news.rbk;

import java.util.LinkedList;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.vasilev.news.actualizer.FeedActualizer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@Component
@RequiredArgsConstructor
public class RbkFeedActualizer implements FeedActualizer {

    private static final float PAGE_SIZE_MULTIPLIER = 1.5f;

    private final RbkFeedLoader rbkFeedLoader;

    @Value("${sources.rbk.page-size}")
    private int pageSize;

    @Override
    public String sourceName() {
        return RbkConst.SOURCE_NAME;
    }

    // Алгоритм загрузки свежих новостей:
    // 0. val itemsFromRbk = <from network>
    // 1. val itemsToSave = itemsFromRbk.fillter (item.date >= lastSavedItem.date)
    // 2. val savedItemsCount = repo.saveAll(itemsToSave).size()
    // 3. val needLoadMore = savedItemsCount == pageSize, т.е. если сохранили всё что пришло = проверить есть ли ещё
    @Override
    public void actualize() {
        log.info(">> start fetching");

        Queue<Integer> q = new LinkedList<>();
        q.add(pageSize);

        while (!q.isEmpty()) {
            int pageSizeToLoad = q.poll();
            int savedItemsCount = rbkFeedLoader.load(pageSizeToLoad);

            // 3
            val needLoadMore = savedItemsCount == pageSize;
            if (needLoadMore) {
                int newPageSize = (int) (pageSizeToLoad * PAGE_SIZE_MULTIPLIER);
                q.add(newPageSize);
            }
        }

        log.info(">> end fetching");
    }
}
