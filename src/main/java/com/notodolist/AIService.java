package com.notodolist;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model:gpt-5-mini}")
    private String model;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpClient httpClient =
            HttpClient.newHttpClient();

    public Map<String, String> suggestTask(String description)
            throws Exception {

        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("model", model);

        requestBody.put(
                "instructions",
                """
                You are an AI assistant for a Not-To-Do productivity application.

                Analyze the user's task description.

                Determine:
                1. The appropriate priority: LOW, MEDIUM, or HIGH.
                2. A suitable group/category.
                3. A short reason explaining the recommendation.

                The task represents something the user wants to avoid.

                Examples of groups:
                Social Media
                Entertainment
                Procrastination
                Food
                Work
                Shopping
                Technology
                Other

                Return only the structured JSON response.
                """
        );

        requestBody.put("input", description);

        Map<String, Object> schema = new HashMap<>();

        schema.put("type", "object");

        Map<String, Object> properties = new HashMap<>();

        properties.put(
                "priority",
                Map.of("type", "string","enum",new String[]{"LOW", "MEDIUM", "HIGH"}
                )
        );

        properties.put(
                "group",
                Map.of(
                        "type", "string"
                )
        );

        properties.put(
                "reason",
                Map.of(
                        "type", "string"
                )
        );

        schema.put("properties", properties);

        schema.put(
                "required",
                new String[]{
                        "priority",
                        "group",
                        "reason"
                }
        );

        schema.put("additionalProperties", false);

        Map<String, Object> jsonSchema = new HashMap<>();

        jsonSchema.put("type", "json_schema");
        jsonSchema.put("name", "task_suggestion");
        jsonSchema.put("strict", true);
        jsonSchema.put("schema", schema);

        requestBody.put(
                "text",
                Map.of(
                        "format",
                        jsonSchema
                )
        );

        String requestJson =
                objectMapper.writeValueAsString(requestBody);

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        "https://api.openai.com/v1/responses"
                                )
                        )
                        .header(
                                "Authorization",
                                "Bearer " + apiKey
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers.ofString(
                                        requestJson
                                )
                        )
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() < 200 ||
                response.statusCode() >= 300) {

            throw new RuntimeException(
                    "OpenAI API error: " +
                    response.statusCode() +
                    " " +
                    response.body()
            );
        }

        JsonNode root =
                objectMapper.readTree(response.body());

        /*
         * Responses API returns generated content
         * inside output -> content -> text.
         */
        JsonNode outputText =
                root.path("output")
                        .path(1)
                        .path("content")
                        .path(0)
                        .path("text");

        if (outputText.isMissingNode()) {
            throw new RuntimeException(
                    "AI response did not contain output text"
            );
        }

        String jsonText = outputText.asText();

        return objectMapper.readValue(
                jsonText,
                Map.class
        );
    }
}