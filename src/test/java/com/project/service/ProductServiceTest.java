package com.project.service;

import com.project.dto.ProductExpiryDTO;
import com.project.entity.Product;
import com.project.repository.InvoiceItemRepository;
import com.project.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InvoiceItemRepository invoiceItemRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");
        product.setHscode("1234");
        product.setSalePrice(BigDecimal.valueOf(100));
        product.setStock(10);
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> result = productService.getAllProducts();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1);

        assertTrue(result.isPresent());
        assertEquals(product, result.get());
    }

    @Test
    void testUpdateProductStock() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product updatedProduct = productService.updateProductStock(1, 20);

        assertEquals(20, updatedProduct.getStock());
    }

    @Test
    void testGetAllProductsWithEarliestExpiry() {
        // Create multiple dummy entries
        List<Object[]> mockResult = List.of(
                new Object[]{
                        1, // productId
                        "Product A", // productName
                        "1234", // hsCode
                        BigDecimal.valueOf(100), // price
                        "Category A", // categoryName
                        "Supplier A", // supplierName
                        LocalDate.now().plusDays(5), // closestExpiryDate
                        10, // quantityAtClosestExpiryDate
                        20 // totalStock
                },
                new Object[]{
                        2, // productId
                        "Product B", // productName
                        "5678", // hsCode
                        BigDecimal.valueOf(150), // price
                        "Category B", // categoryName
                        "Supplier B", // supplierName
                        LocalDate.now().plusDays(10), // closestExpiryDate
                        15, // quantityAtClosestExpiryDate
                        30 // totalStock
                },
                new Object[]{
                        3, // productId
                        "Product C", // productName
                        "91011", // hsCode
                        BigDecimal.valueOf(200), // price
                        "Category C", // categoryName
                        "Supplier C", // supplierName
                        LocalDate.now().plusDays(15), // closestExpiryDate
                        5, // quantityAtClosestExpiryDate
                        25 // totalStock
                }
        );

        when(productRepository.findAllProductsWithEarliestExpirationDateSorted())
                .thenReturn(mockResult);

        List<ProductExpiryDTO> result = productService.getAllProductsWithEarliestExpiry();

        // Assertions
        assertFalse(result.isEmpty());
        assertEquals(3, result.size());

        ProductExpiryDTO dto1 = result.get(0);
        assertEquals(1, dto1.getProductId());
        assertEquals("Product A", dto1.getProductName());
        assertEquals("1234", dto1.getHsCode());
        assertEquals(0, BigDecimal.valueOf(100).compareTo(dto1.getPrice()));
        assertEquals("Category A", dto1.getCategoryName());
        assertEquals("Supplier A", dto1.getSupplierName());
        assertEquals(LocalDate.now().plusDays(5), dto1.getClosestExpiryDate());
        assertEquals(10, dto1.getQuantityAtClosestExpiryDate());
        assertEquals(20, dto1.getTotalStock());

        ProductExpiryDTO dto2 = result.get(1);
        assertEquals(2, dto2.getProductId());
        assertEquals("Product B", dto2.getProductName());
        assertEquals("5678", dto2.getHsCode());
        assertEquals(0, BigDecimal.valueOf(150).compareTo(dto2.getPrice()));
        assertEquals("Category B", dto2.getCategoryName());
        assertEquals("Supplier B", dto2.getSupplierName());
        assertEquals(LocalDate.now().plusDays(10), dto2.getClosestExpiryDate());
        assertEquals(15, dto2.getQuantityAtClosestExpiryDate());
        assertEquals(30, dto2.getTotalStock());

        ProductExpiryDTO dto3 = result.get(2);
        assertEquals(3, dto3.getProductId());
        assertEquals("Product C", dto3.getProductName());
        assertEquals("91011", dto3.getHsCode());
        assertEquals(0, BigDecimal.valueOf(200).compareTo(dto3.getPrice()));
        assertEquals("Category C", dto3.getCategoryName());
        assertEquals("Supplier C", dto3.getSupplierName());
        assertEquals(LocalDate.now().plusDays(15), dto3.getClosestExpiryDate());
        assertEquals(5, dto3.getQuantityAtClosestExpiryDate());
        assertEquals(25, dto3.getTotalStock());
    }
}
