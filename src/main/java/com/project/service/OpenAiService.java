package com.project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public OpenAiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String describeImage(MultipartFile imageFile) throws IOException {
        String base64Image = encodeImage(imageFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "gpt-4o-mini");
        payload.put("messages", List.of(
                Map.of("role", "user", "content", List.of(
                        Map.of("type", "text", "text", "Please extract the following details from the invoice data and provide the information in JSON format:\n" +
                                "\n" +
                                "- Company name\n" +
                                "- Country where the company is located\n" +
                                "- Company address postcode\n" +
                                "- Order date\n" +
                                "- Total price\n" +
                                "- Product name\n" +
                                "- Hs code\n" +
                                "- Quantity of the product\n" +
                                "- Unit price of the product\n" +
                                "\n" +
                                "The JSON format should include all these details organized under appropriate keys.\n"),
                        Map.of("type", "image_url", "image_url", Map.of("url", "data:image/jpeg;base64," + base64Image))
                ))
        ));
        payload.put("max_tokens", 300);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.openai.com/v1/chat/completions",
                HttpMethod.POST,
                request,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return extractContent(response.getBody());
        } else {
            throw new RuntimeException("Failed to call OpenAI API. Status code: " + response.getStatusCode());
        }
    }

    private String encodeImage(MultipartFile imageFile) throws IOException {
        byte[] imageBytes = imageFile.getBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private String extractContent(Map<String, Object> responseBody) {
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            if (message != null) {
                return (String) message.get("content");
            }
        }
        return "No content found";
    }
}
