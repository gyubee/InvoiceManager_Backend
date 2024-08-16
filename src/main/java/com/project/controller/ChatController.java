package com.project.controller;

import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/invoice")
public class ChatController {

    private final OpenAiChatModel chatModel;

    @Autowired
    public ChatController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/ai/text")
    public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("response", chatModel.call(message));
    }

    @PostMapping(value = "/ai/image/url")
    public Map<String, Object> describeImage(@RequestBody String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new IllegalArgumentException("Image URL cannot be null or empty.");
        }

        UserMessage userMessage = new UserMessage(
                "what do you see in the photo?",
                List.of(new Media(MimeTypeUtils.IMAGE_PNG, imageUrl))
        );

        Prompt prompt = new Prompt(List.of(userMessage),
                OpenAiChatOptions.builder().withModel(OpenAiApi.ChatModel.GPT_4_O.getValue()).build());

        try {
            ChatResponse response = chatModel.call(prompt);
            return Map.of("description", response);
        } catch (NonTransientAiException e) {
            return Map.of("error", "Failed to process the image. Please check the image URL and format.");
        }
    }

    @GetMapping("/ai/image/local")
    public Map<String, Object> describeImageFromLocal() {
        String imagePath = "img/invoice2.png"; // Use relative path within resources
        ClassPathResource resource = new ClassPathResource(imagePath);

        try {
            byte[] imageData = resource.getInputStream().readAllBytes();
            UserMessage userMessage = new UserMessage(
                    "Please extract the following details from the invoice data and provide the information in JSON format:\n" +
                            "\n" +
                            "- Company name\n" +
                            "- Country where the company is located\n" +
                            "- Company address postcode\n" +
                            "- Order date\n" +
                            "- Total price\n" +
                            "- Product name\n" +
                            "- Product code\n" +
                            "- Quantity of the product\n" +
                            "- Unit price of the product\n" +
                            "\n" +
                            "The JSON format should include all these details organized under appropriate keys.\n",
                    List.of(new Media(MimeTypeUtils.IMAGE_PNG, imageData))
            );

            Prompt prompt = new Prompt(List.of(userMessage),
                    OpenAiChatOptions.builder().withModel(OpenAiApi.ChatModel.GPT_4_O.getValue()).build());

            ChatResponse response = chatModel.call(prompt);
            return Map.of("invoice information", response.getResult().getOutput().getContent());

        } catch (IOException e) {
            return Map.of("error", "Failed to read the local file. Please make sure the file exists and the path is correct.");
        } catch (NonTransientAiException e) {
            return Map.of("error", "Failed to process the image. Please ensure it is a valid format.");
        }
    }

}
