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

    public String extractInvoice(MultipartFile imageFile) throws IOException {
        String base64Image = encodeImage(imageFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "gpt-4o-mini");
        payload.put("messages", List.of(
                Map.of("role", "user", "content", List.of(
                        Map.of("type", "text", "text", "Please extract the following details from the invoice data complete the JSON data\n" +
                                "\n" +
"                                {\n" +
"                                            \"receive_date\": \"\",\n" +
"                                            \"total_price\": \"\",\n" +
"                                            \"company_name\": \"\",\n" +
"                                            \"company_address\": \"\",\n" +
"                                            \"products\": [\n" +
"                                    {\n" +
"                                        \"product_name\": \"\",\n" +
"                                            \"hscode\": \"\",\n" +
"                                            \"quantity\": ,\n" +
"                                            \"unit_price\": \"\",\n" +
"                                            \"expiration_date\": \"\"\n" +
"                                    },\n" +
"                              ]\n" +
"                            }\n"+
                                "\n" +
                                "Do not include any other word or information other than the json data and is there is non found, leave it as blank or null \n"),
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
                String content = (String) message.get("content");

                // Remove the "```json" and "```" markers
                if (content != null) {
                    content = content.replace("```json", "").replace("```", "").trim();
                }
                return content;
            }
        }
        return "No content found";
    }
}
