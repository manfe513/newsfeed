package com.vasilev.news.service;

import com.vasilev.news.model.domain.Source;
import com.vasilev.news.model.domain.SourceRules;
import com.vasilev.news.model.request.AddSourceRequest;
import com.vasilev.news.model.db.SourceEntity;
import com.vasilev.news.model.db.SourceRulesEntity;
import com.vasilev.news.model.request.AddSourceRequestRules;
import com.vasilev.news.repository.SourcesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SourcesService {

    private final SourcesRepository repo;

    @Transactional
    public void addSource(AddSourceRequest request) {
        repo.save(
                toSourceEntity(request)
        );

        repo.deleteAllInBatch(List.of());
    }

    @Transactional
    public void removeSource(Integer id) {
        repo.deleteAllById(List.of(id));
    }

    @Transactional(readOnly = true)
    public List<Source> getAll() {
        return repo.findAll().stream()
                .map(this::fromSourceEntity).toList();
    }

    private SourceEntity toSourceEntity(AddSourceRequest req) {
        AddSourceRequestRules reqRules = req.rules();

        return SourceEntity.builder()
                .name(req.name())
                .url(req.url())
                .rules(
                        SourceRulesEntity.builder()
                                .itemPath(reqRules.itemPath())
                                .titlePath(reqRules.titlePath())
                                .descriptionPath(reqRules.descriptionPath())
                                .datePath(reqRules.datePath())
                                .dateFormat(reqRules.dateFormat())
                                .build()
                )
                .build();
    }

    private Source fromSourceEntity(SourceEntity entity) {
        SourceRulesEntity rulesEntity = entity.getRules();

        return Source.builder()
                .id(entity.getId())
                .name(entity.getName())
                .url(entity.getUrl())
                .rules(
                        SourceRules.builder()
                                .id(rulesEntity.getId())
                                .itemPath(rulesEntity.getItemPath())
                                .titlePath(rulesEntity.getTitlePath())
                                .descriptionPath(rulesEntity.getDescriptionPath())
                                .datePath(rulesEntity.getDatePath())
                                .dateFormat(rulesEntity.getDateFormat())
                                .build()
                )
                .build();
    }
}
