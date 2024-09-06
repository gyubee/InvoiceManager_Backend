//controller/ChatController
package com.project.controller;


import com.project.dto.InvoiceDTO;
import com.project.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.project.service.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/invoice")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final OpenAiChatModel chatModel;

    private final OpenAiService openAiService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public ChatController(OpenAiChatModel chatModel, OpenAiService openAiService) {
        this.chatModel = chatModel;
        this.openAiService = openAiService;
    }

    // Static JSON
    private static final String TEST_JSON1 = """
    {
      "company_name": "ABC Exports",
      "country": "United States",
      "company_address": "example Rd, California, USA",
      "company_email": "company@email.com",
      "receive_date": "04 Jul 2022",
      "total_price": "19,860.00",
      "products": [
        {
          "product_name": "BAR STOOL ALUMINIUM",
          "hs_code": "3926.00.00",
          "quantity": 150,
          "unit_price": "77.20"
        },
        {
          "product_name": "BAR TABLE ALUMINIUM",
          "hs_code": "3926.00.00",
          "quantity": 75,
          "unit_price": "110.40"
        }
      ]
    }
    """;


    private static final String TEST_JSON2 = """
    {
        "company_name": "Upela",
        "country": "France",
        "company_address_postcode": "75008",
        "order_date": "22 Feb 2024",
        "total_price": "230.00",

      "products": [
        {
          "product_name": "Painting made with acrylic paint",
          "hs_code": "4001",
          "quantity": 1,
          "unit_price": "80.00"
        },
        {
          "product_name": "Painting made with acrylic paint",
          "hs_code": "4201 - ",
          "quantity": 1,
          "unit_price": "70.00"
        },
        {
          "product_name": "Painting done with gouache",
          "hs_code": "4401",
          "quantity": 1,
          "unit_price": "80.00"
        }
      ]
    }
    """;


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






//    @GetMapping("/ai/image/local")
//    public Map<String, Object> describeImageFromLocal(@RequestParam(defaultValue = "false") boolean save) {
//        String imagePath = "img/invoice3.png"; // Use relative path within resources
//        ClassPathResource resource = new ClassPathResource(imagePath);
//
//        try {
//            byte[] imageData = resource.getInputStream().readAllBytes();
//            UserMessage userMessage = new UserMessage(
//                    "Please extract the following details from the invoice data and provide the information in JSON format:\n" +
//                            "\n" +
//                            "- Company name\n" +
//                            "- Country where the company is located\n" +
//                            "- Company address postcode\n" +
//                            "- Order date\n" +
//                            "- Total price\n" +
//                            "- Product name\n" +
//                            "- Hs code\n" +
//                            "- Quantity of the product\n" +
//                            "- Unit price of the product\n" +
//                            "\n" +
//                            "The JSON format should include all these details organized under appropriate keys.\n",
//                    List.of(new Media(MimeTypeUtils.IMAGE_PNG, imageData))
//            );
//
//            Prompt prompt = new Prompt(List.of(userMessage),
//                    OpenAiChatOptions.builder().withModel(OpenAiApi.ChatModel.GPT_4_O.getValue()).build());
//
//            ChatResponse response = chatModel.call(prompt);
//            String jsonString = response.getResult().getOutput().getContent();
//
//            jsonString = jsonString.replace("```json", "").replace("```", "").trim();
//
//            Map<String, Object> invoiceData = objectMapper.readValue(jsonString, Map.class);
//
//            if (save) {
//                invoiceService.saveInvoiceData(invoiceData);
//                invoiceData.put("status", "Invoice data saved successfully");
//            } else {
//                invoiceData.put("status", "Invoice data extracted but not saved");
//            }
//
//            logger.info("AI Response: {}", invoiceData);
//            return invoiceData;
//        } catch (IOException e) {
//            logger.error("File read error", e);
//            return Map.of("error", "Failed to read the local file. Please make sure the file exists and the path is correct.");
//        } catch (NonTransientAiException e) {
//            logger.error("AI processing error", e);
//            return Map.of("error", "Failed to process the image. Please ensure it is a valid format.");
//        } catch (Exception e) {
//            logger.error("Unexpected error", e);
//            return Map.of("error", "An unexpected error occurred: " + e.getMessage());
//        }
//    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/ai/image/upload")
    public String describeImage(@RequestParam("image") MultipartFile imageFile) {
        try {
            return openAiService.extractInvoice(imageFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to process the image", e);
        }
    }

    @GetMapping("/add")
    public String home() {
        return "AAAAAAAAA";
    }


    @GetMapping("/test/1")
    public String test1() throws JsonProcessingException {


        Map<String, Object> invoiceData = objectMapper.readValue(TEST_JSON1, Map.class);
        invoiceService.saveInvoiceData(invoiceData);



        return "Test 1 Successful";
    }

    @GetMapping("/test/2")
    public String test2() throws JsonProcessingException {


        Map<String, Object> invoiceData = objectMapper.readValue(TEST_JSON2, Map.class);
        invoiceService.saveInvoiceData(invoiceData);



        return "NNNN";
    }


//    @GetMapping("/products")
//    public ResponseEntity<List<ProductDTO>> getAllProducts() {
//        List<Product> products = invoiceService.getAllProductsDTO();
//        return ResponseEntity.ok(products);
//    }

//    @GetMapping("/get/invoices")
//    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
//        List<InvoiceDTO> invoices = invoiceService.getAllInvoicesDTO();
//        return ResponseEntity.ok(invoices);
//    }
//
//    @GetMapping("/get/invoices/{id}")
//    public ResponseEntity<InvoiceDTO> getInvoiceWithProducts(@PathVariable Long id) {
//        try {
//            InvoiceDTO invoice = invoiceService.getInvoiceWithProductsDTO(id);
//            return ResponseEntity.ok(invoice);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

}
