package com.sensitivewords.assessment.controller;

import com.sensitivewords.assessment.dto.MessageRequest;
import com.sensitivewords.assessment.dto.MessageResponse;
import com.sensitivewords.assessment.service.SensitiveWordService;
import org.springframework.web.bind.annotation.*;
import com.sensitivewords.assessment.database.repository.SensitiveWordRepository;
import com.sensitivewords.assessment.database.entity.SensitiveWord;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/sensitivewords")
public class SensitiveWordController {
    private final SensitiveWordService service;
    private final SensitiveWordRepository repository;

    public SensitiveWordController(SensitiveWordService service, SensitiveWordRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Operation(summary = "Filter sensitive words from a message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered message returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request",content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/filter")
    public ResponseEntity<MessageResponse> filterMessage(@RequestBody MessageRequest request) {
        String filtered = service.filterMessage(request.getMessage());
        return ResponseEntity.ok(new MessageResponse(filtered));
    }


     @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Word added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request",content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/add")
    public ResponseEntity<URI> addWord(@RequestBody SensitiveWord word) {
        SensitiveWord saved = repository.save(word);
        URI location = URI.create("api/v1/sensitivewords" + Objects.requireNonNull(saved.getId(), "Saved entity id cannot be null"));
        return ResponseEntity.ok(location);//created(Objects.requireNonNull(location, "URI is null")).body(saved);
    }

    @Operation(summary = "Delete a sensitive word by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Word deleted"),
            @ApiResponse(responseCode = "404", description = "Word not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteWord(@PathVariable Long id) {
    
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all sensitive words")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned")
    })
    @GetMapping
    public ResponseEntity<List<SensitiveWord>> list() {
        List<SensitiveWord> list = repository.findAll();
        return ResponseEntity.ok(list);
    }
}