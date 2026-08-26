package com.notodolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:5173")
public class AIController {

    private final AIService aiService;

    @Autowired
    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/suggest")
    public ResponseEntity<?> suggestTask(@RequestBody Map<String, String> request) {

        String description = request.get("description");

        if (description == null || description.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "error",
                            "Task description is required"
                    ));
        }

        try {

            Map<String, String> suggestion =
                    aiService.suggestTask(description);

            return ResponseEntity.ok(suggestion);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "error",
                            "Unable to generate AI suggestion"
                    ));
        }
    }
}