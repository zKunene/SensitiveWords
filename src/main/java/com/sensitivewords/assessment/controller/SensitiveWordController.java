package com.sensitivewords.assessment.controller;

import com.sensitivewords.assessment.dto.MessageRequest;
import com.sensitivewords.assessment.dto.MessageResponse;
import com.sensitivewords.assessment.service.SensitiveWordService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> addWord(@RequestBody String word) {
        if (service.addSensitiveWord(word)) {
            return ResponseEntity.ok("Word successfully added");
        }
        return ResponseEntity.ok("Word already created in DB");
    }

    @Operation(summary = "Delete a sensitive word by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Word deleted"),
            @ApiResponse(responseCode = "404", description = "Word not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWord(@PathVariable Long id) {
    
        if (service.removeSensitiveWord(id) ==true) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Word not found");
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

    @Operation(summary = "Get sensitive word by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Word returned"),
            @ApiResponse(responseCode = "404", description = "Word not found")
    })
    @GetMapping("findWord/{id}")
    public ResponseEntity<?> findWord(@PathVariable Long id) {
        final ResponseEntity<?>[] response = new ResponseEntity[1];
        repository.findById(id).ifPresentOrElse(word -> 
            {
                response[0] = ResponseEntity.ok(word);
            }, () -> {
                response[0] = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Word not found");
            });
       return response[0];
    }
}