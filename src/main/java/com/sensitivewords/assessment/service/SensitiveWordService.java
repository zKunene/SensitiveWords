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
        //Load words from file

        List<String> fileWords = loadFromClasspathFile("sql_sensitive_list.txt");
        log.info("loading words from file");
        for (String word : fileWords) {
            if (!sensitiveWords.contains(word)) {
                sensitiveWords.add(word);
            }
        }

        //Load from DB
        List<String> dbWords = repository.findAll().stream()
                    .map(SensitiveWord::getWord)
                    .collect(Collectors.toList());
        log.info("Readiing in words from DB");
        for (String word : dbWords) {
            if (!sensitiveWords.contains(word)) {
                sensitiveWords.add(word);
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

    private List<String> loadFromClasspathFile(String resourceName) {
        try {
            ClassPathResource resource = new ClassPathResource(Objects.requireNonNull(resourceName, "Resource cannot be null"));
            if (!resource.exists()) {
                return List.of();
            }
            try (InputStream is = resource.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    
                String content = br.lines().collect(Collectors.joining());
                content = content.replace("[","").replace("]","").trim();
                if (content.isEmpty()) {
                    return List.of();
                }

                String[] parts = content.split(",");
                return Pattern.compile("\\r?\\n").splitAsStream(String.join(",",parts)).map(s -> s.replace("\"", "").trim()).filter(s -> !s.isEmpty()).collect(Collectors.toList());

                }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource: " + resourceName, e);
        }
    }
    
}