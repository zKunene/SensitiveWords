package com.sensitivewords.assessment.integration;

import com.sensitivewords.assessment.entity.SensitiveWord;
import com.sensitivewords.assessment.repository.SensitiveWordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.fatory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ActiveProfiles("integration")
@DataJpaTest
class SensitiveWordRepositoryTest {
    
    @Autowired
    private SensitiveWordRepository repository;

    @BeforeEach
    void setup() {repository.deleteAll();}

    @Test
    @Transactional
    @Rollback
    void findByWordTest() {
        SensitiveWord entity = SensitiveWord.builder().word("SELECT").build();
        repository.save(entity);

        String test = "SELECT";
        String test2 = "select"

        var testFind = repository.findByWord(test);
        assertTrue(testFind.isPresent());
        assertEquals(testFind.getWord(), test);

        var testFind2 - repository.findByWord(test2.toUpperCase());
        assertTrue(testFind2.isPresent());
        assertEquals(testFind2.getWord(), test2.toUpperCase());

    }

}