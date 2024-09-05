package com.project.controller;

import com.project.dto.ItemDiscountDTO;
import com.project.dto.ProductExpiryDTO;
import com.project.entity.Product;
import com.project.service.DiscountService;
import com.project.service.InvoiceItemService;
import com.project.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private InvoiceItemService invoiceItemService;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testGetAllProducts() throws Exception {
        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");

        when(productService.getAllProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/invoicemanager/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Test Product"));
    }

    @Test
    void testGetProductById() throws Exception {
        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");

        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/invoicemanager/products/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Test Product"));
    }

    @Test
    void testGetAllProductsSortedByStock() throws Exception {
        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");

        when(productService.getAllProductsSortedByStock()).thenReturn(List.of(product));

        mockMvc.perform(get("/invoicemanager/products/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Test Product"));
    }

    @Test
    void testGetAllProductsWithStock() throws Exception {
        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");

        when(productService.getAllProductsWithStock()).thenReturn(List.of(product));

        mockMvc.perform(get("/invoicemanager/products/all/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Test Product"));
    }

    @Test
    void testGetStockByProductId() throws Exception {
        when(productService.getStockByProductId(1)).thenReturn(10);

        mockMvc.perform(get("/invoicemanager/products/{id}/stock", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    void testUpdateProductStock() throws Exception {
        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");
        product.setStock(15);

        when(productService.updateProductStock(1, 15)).thenReturn(product);

        mockMvc.perform(put("/invoicemanager/products/{id}/stock", 1)
                        .param("newStock", "15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(15));
    }

    @Test
    void testGetAllProductsWithEarliestExpiry() throws Exception {
        ProductExpiryDTO dto = new ProductExpiryDTO();
        dto.setProductName("Test Product");

        when(productService.getAllProductsWithEarliestExpiry()).thenReturn(List.of(dto));

        mockMvc.perform(get("/invoicemanager/products/expiry"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Test Product"));
    }

    @Test
    void testFindDiscountDetailsByProductId() throws Exception {
        ItemDiscountDTO dto = new ItemDiscountDTO(1, 10.0f, 1, BigDecimal.valueOf(100), LocalDate.now().plusDays(5));

        when(invoiceItemService.findDiscountDetailsByProductId(1)).thenReturn(List.of(dto));

        mockMvc.perform(get("/invoicemanager/products/{productId}/expiry", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].unitPrice").value(100));
    }

    @Test
    void testApplyDiscount() throws Exception {
        doNothing().when(discountService).applyDiscountToInvoiceItem(1, 1, 10.0f);

        mockMvc.perform(post("/invoicemanager/products/{productId}/{invoiceItemId}/discounts/apply", 1, 1)
                        .param("discountRate", "10.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("Discount applied successfully."));
    }

    @Test
    void testApplyGlobalDiscount() throws Exception {
        doNothing().when(discountService).applyGlobalDiscountBasedOnExpiration(30, 10.0f);

        mockMvc.perform(post("/invoicemanager/products/discount/global")
                        .param("daysLeftTillExpire", "30")
                        .param("discountRate", "10.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("Global discount applied successfully."));
    }

}
