package com.vasilev.news.repository;

import com.vasilev.news.model.db.FeedEntity;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Repository
public interface FeedRepository extends JpaRepository<FeedEntity, Integer> {

    List<FeedEntity> findAllByDateBeforeOrderByDateDesc(Instant date, Limit limit);

    @Query(value = """
    SELECT *
    FROM feed f
    WHERE f.date <= :date
    AND to_tsvector('russian', title || ' ' || description) @@ plainto_tsquery('russian', CONCAT(:searchText, ':*'))
    """, nativeQuery = true)
    List<FeedEntity> findAllBySearch(
            @Param("date") Instant date,
            @Param("searchText") String searchText,
            Limit limit
    );

    Optional<FeedEntity> findFirstBySourceOrderByDateDesc(String source);
}
