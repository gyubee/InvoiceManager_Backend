package com.project.repository;

import com.project.entity.Category;
import com.project.entity.Company;
import com.project.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Category category = new Category();
        category.setCategoryId(1);

        Company supplier = new Company();
        supplier.setCompanyId(1);

        product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");
        product.setHscode("1234");
        product.setSalePrice(BigDecimal.valueOf(100));
        product.setStock(10);
        product.setCategory(category);
        product.setSupplier(supplier);
    }

    @Test
    void testFindByProductName() {
        when(productRepository.findByProductName("Test Product"))
                .thenReturn(Optional.of(product));

        Optional<Product> result = productRepository.findByProductName("Test Product");

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getProductName());
    }

    @Test
    void testFindAllByOrderByStockAsc() {
        List<Product> products = new ArrayList<>();
        products.add(product);

        when(productRepository.findAllByOrderByStockAsc())
                .thenReturn(products);

        List<Product> result = productRepository.findAllByOrderByStockAsc();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
    }

    @Test
    void testFindAllWithStock() {
        List<Product> products = new ArrayList<>();
        products.add(product);

        when(productRepository.findAllWithStock())
                .thenReturn(products);

        List<Product> result = productRepository.findAllWithStock();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
    }

    @Test
    void testFindStockByProductId() {
        when(productRepository.findStockByProductId(1))
                .thenReturn(10);

        Integer result = productRepository.findStockByProductId(1);

        assertEquals(10, result);
    }

    @Test
    void testFindAllProductsWithEarliestExpirationDateSorted() {

        List<Object[]> productsWithEarliestExpiration = new ArrayList<>();
        productsWithEarliestExpiration.add(new Object[]{
                1, // productId
                "Test Product", // productName
                "1234", // hscode
                BigDecimal.valueOf(100), // price
                "Test Category", // categoryName
                "Test Supplier", // supplierName
                LocalDate.now().plusDays(5), // closestExpiryDate
                10, // quantityAtClosestExpiryDate
                10 // totalStock
        });

        when(productRepository.findAllProductsWithEarliestExpirationDateSorted())
                .thenReturn(productsWithEarliestExpiration);

        List<Object[]> result = productRepository.findAllProductsWithEarliestExpirationDateSorted();

        assertFalse(result.isEmpty());
        assertEquals("Test Product", result.get(0)[1]); // productName
    }
}
