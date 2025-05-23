package com.serchaleSoogle.api_soogle.model.dao;

import com.serchaleSoogle.api_soogle.model.entity.SearchResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchResultRepository extends JpaRepository<SearchResultEntity, Long> {
    List<SearchResultEntity> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String titleQuery, String descriptionQuery);

}
