package com.sensitivewords.assessment.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sensitivewords.assessment.database.entity.SensitiveWord;

import java.util.Optional;

@Repository
public interface SensitiveWordRepository extends JpaRepository<SensitiveWord, Long> {
    //@Query("SELECT * FROM dbo.sensitiveword_db WHERE word =:word")
    Optional<SensitiveWord> findByWord(String word);
}