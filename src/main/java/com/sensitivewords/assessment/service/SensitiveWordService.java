package com.sensitivewords.assessment.service;

import com.sensitivewords.assessment.database.entity.SensitiveWord;
import com.sensitivewords.assessment.database.repository.SensitiveWordRepository;


import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


@Slf4j
@Service
public class SensitiveWordService {
    private final SensitiveWordRepository repository;
    private final List<String> sensitiveWords = new ArrayList<>();
    private List<Pattern> patterns = new ArrayList<>();

    public SensitiveWordService(SensitiveWordRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {

        //Load from DB
        
        //Load words from file
        List<String> fileWords = loadFromClasspathFile("sql_sensitive_list.txt");
        log.info("loading words from file");
        for (String word : fileWords) {
            if (!sensitiveWords.contains(word)) {
                sensitiveWords.add(word);
                if (repository.findByWord(word.toUpperCase()).isEmpty()) {
                    repository.save(Objects.requireNonNull(SensitiveWord.builder().word(word).build()));
                }
            }
        }

        //Regex patterns
        patterns = sensitiveWords.stream()
                    .map(word -> Pattern.compile("(?i)\\b" + word + "\\b"))
                    .collect(Collectors.toList());
    }

    public String filterMessage(String  message) {
        String result = message;
        for (Pattern p : patterns) {
            result = p.matcher(result).replaceAll(match -> "*".repeat(match.group().length()));
        }
        log.info("Message filtering successful");
        return result;
    }

    private List<String> loadFromClasspathFile(final String resourceName) {
        List<String> words = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(Objects.requireNonNull(resourceName, "Resource cannot be null"));
    
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line=reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("["))
                    line = line.substring(1);

                if (line.endsWith("]"))
                    line = line.substring(0, line.length()-1);
                
                if (line.startsWith(","))
                    line = line.substring(1).trim();

                if (line.startsWith("\"") && line.endsWith("\""))
                    line = line.substring(1, line.length() -1);

                if (line.length() !=0) {
                    words.add(line);
                }
                
            }
          return words;      

        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource: " + resourceName, e);
        }
    }

    public boolean addSensitiveWord(final String word) {
        if (word.length() > 0 && repository.findByWord(word.toUpperCase()).isEmpty()) {
            repository.save(SensitiveWord.builder().word(word).build());
            refresh();
            return true;
        }
        else {
            return false;
        }

    }

    public boolean removeSensitiveWord(final Long id) {
        boolean exists = repository.existsById(id);
        if (!exists) {
            return false;
        }
        repository.deleteById(id);
        refresh();
        return true;
    }

    public void refresh() {
        //Load from DB
        sensitiveWords.clear();
        List<String> dbWords = repository.findAll().stream()
                    .map(SensitiveWord::getWord)
                    .collect(Collectors.toList());
        log.info("Reading in words from DB");
        for (String word : dbWords) {
            if (!sensitiveWords.contains(word)) {
                sensitiveWords.add(word.toUpperCase());

            }
        }
    }
    
}