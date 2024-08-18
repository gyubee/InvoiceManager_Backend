//controller/ChatController
package com.project.controller;


import com.project.dto.InvoiceDTO;
import com.project.dto.ProductDTO;
import com.project.entity.Invoice;
import com.project.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.project.service.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/invoice")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final OpenAiChatModel chatModel;


    @Autowired
    private InvoiceService invoiceService;


    @Autowired
    private ObjectMapper objectMapper;

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
    public Map<String, Object> describeImageFromLocal(@RequestParam(defaultValue = "false") boolean save) {
        String imagePath = "img/invoice3.png"; // Use relative path within resources
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
                            "- Hs code\n" +
                            "- Quantity of the product\n" +
                            "- Unit price of the product\n" +
                            "\n" +
                            "The JSON format should include all these details organized under appropriate keys.\n",
                    List.of(new Media(MimeTypeUtils.IMAGE_PNG, imageData))
            );

            Prompt prompt = new Prompt(List.of(userMessage),
                    OpenAiChatOptions.builder().withModel(OpenAiApi.ChatModel.GPT_4_O.getValue()).build());

            ChatResponse response = chatModel.call(prompt);
            String jsonString = response.getResult().getOutput().getContent();

            // Remove the JSON code block markers if present
            jsonString = jsonString.replace("```json", "").replace("```", "").trim();

            Map<String, Object> invoiceData = objectMapper.readValue(jsonString, Map.class);

            // If save parameter is true, save the data
            if (save) {
                invoiceService.saveInvoiceData(invoiceData);
                invoiceData.put("status", "Invoice data saved successfully");
            } else {
                invoiceData.put("status", "Invoice data extracted but not saved");
            }

            logger.info("AI Response: {}", invoiceData);
            return invoiceData;
        } catch (IOException e) {
            logger.error("File read error", e);
            return Map.of("error", "Failed to read the local file. Please make sure the file exists and the path is correct.");
        } catch (NonTransientAiException e) {
            logger.error("AI processing error", e);
            return Map.of("error", "Failed to process the image. Please ensure it is a valid format.");
        } catch (Exception e) {
            logger.error("Unexpected error", e);
            return Map.of("error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/add")
    public String home() {
        return "AAAAAAAAA";
    }


//    @GetMapping("/products")
//    public ResponseEntity<List<ProductDTO>> getAllProducts() {
//        List<Product> products = invoiceService.getAllProductsDTO();
//        return ResponseEntity.ok(products);
//    }

    @GetMapping("/get/invoices")
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        List<InvoiceDTO> invoices = invoiceService.getAllInvoicesDTO();
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/get/invoices/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceWithProducts(@PathVariable Long id) {
        try {
            InvoiceDTO invoice = invoiceService.getInvoiceWithProductsDTO(id);
            return ResponseEntity.ok(invoice);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
