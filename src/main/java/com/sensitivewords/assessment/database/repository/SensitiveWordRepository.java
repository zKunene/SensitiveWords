package com.sensitivewords.assessment.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sensitivewords.assessment.database.entity.SensitiveWord;

import java.util.Optional;

@Repository
public interface SensitiveWordRepository extends JpaRepository<SensitiveWord, Long> {
    Optional<SensitiveWord> findByWordIgnoreCase(String word);
}