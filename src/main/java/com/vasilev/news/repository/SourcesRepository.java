package com.vasilev.news.repository;

import com.vasilev.news.model.db.SourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SourcesRepository extends JpaRepository<SourceEntity, Integer> {

}
