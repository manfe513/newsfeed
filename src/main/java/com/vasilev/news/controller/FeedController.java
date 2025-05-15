package com.vasilev.news.controller;

import com.vasilev.news.model.domain.FeedListItem;
import com.vasilev.news.service.FeedService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(produces = "application/json")
@Validated
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    /**
     * Получение новостей на момент указанной даты.
     *
     * @param dateFrom - дата, от которой искать новости, в формате строки ISO 8601
     * @param count    - количество новостей
     */
    @GetMapping(
            value = "/feed"
    )
    public ResponseEntity<List<FeedListItem>> feed(
            @RequestParam(name = "dateFrom")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant dateFrom,

            @RequestParam(name = "count")
            @Positive(message = "{validation_count_should_be_positive}")
            int count,

            @RequestParam(name = "searchText", required = false)
            @Nullable
            @Size(min = 2, message = "{validation_search_text_too_small}")
            String searchText
    ) {

        return ResponseEntity.ok(feedService.getFeed(dateFrom, count, searchText));
    }

    @GetMapping(
            value = "actualize"
    )
    public ResponseEntity<Object> actualize(
            @RequestParam(name = "source")
            @NotBlank(message = "{validation_empty_source}")
            String source
    ) {
        feedService.actualizeBySource(source);
        return ResponseEntity.ok(null);
    }
}
