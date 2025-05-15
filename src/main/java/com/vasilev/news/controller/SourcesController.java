package com.vasilev.news.controller;

import com.vasilev.news.model.domain.Source;
import com.vasilev.news.model.request.AddSourceRequest;
import com.vasilev.news.model.request.RemoveSourceRequest;
import com.vasilev.news.service.SourcesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/sources",produces = "application/json")
@Validated
@RequiredArgsConstructor
public class SourcesController {

    private final SourcesService sourcesService;

    /**
     * Добавление источника новостей
     *
     * @param request - данные источника и правила парсинга его новостей
     */
    @PostMapping
    public ResponseEntity<Void> addSource(
            @RequestBody
            AddSourceRequest request
    ) {

        sourcesService.addSource(request);

        return ResponseEntity.ok().build();
    }

    /**
     * Удаление источника новостей
     *
     * @param request - данные источника для удаления
     */
    @PostMapping(path = "/remove")
    public ResponseEntity<Void> removeSource(
            @RequestBody
            RemoveSourceRequest request
    ) {

        sourcesService.removeSource(request.id());

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Source>> getSources() {
        return ResponseEntity.ok(sourcesService.getAll());
    }
}
