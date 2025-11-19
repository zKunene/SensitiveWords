package com.sensitivewords.assessment.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;

@ExtendWith(MockitoExtension.class)
public class SensitiveWordServiceTest {

    @Mock
    private SensitiveWordRepository repository;

    @InjectMocks
    private SensitiveWordService service;

    @Test
    void givenId_removeWord() {
        var entity = SensitiveWord.builder().word("testCase").build();
        repository.save(entity);

        var id = entity.getId();

        boolean blank = service.removeSensitiveWord(id);

        assertTrue(blank);

    }

    @Test
    void givenString_addSensitiveWord() {
        String testCase = "TESTCASE";
        boolean x = service.addSensitiveWord(testCase);
        assertTrue(x);

    }

    @Test
    void givenString_bloopOutMessage() {
        String message = "TESTCASE"
        boolean x = service.addSensitiveWord("TESTCASE");
        assertTrue(x);

        var result = service.filterMessage(message);
        assertEquals("********",result);

    }

}