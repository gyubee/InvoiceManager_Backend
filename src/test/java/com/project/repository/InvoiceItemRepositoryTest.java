package com.project.repository;

import com.project.entity.InvoiceItem;
import com.project.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class InvoiceItemRepositoryTest {

    @Mock
    private InvoiceItemRepository invoiceItemRepository;

    @InjectMocks
    private InvoiceItem invoiceItem;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");

        invoiceItem = new InvoiceItem();
        invoiceItem.setProduct(product);  // Set the entire product entity
        invoiceItem.setExpirationDate(LocalDate.now().plusDays(5));
        invoiceItem.setQuantity(10);
        invoiceItem.setUnitPrice(BigDecimal.valueOf(100));
    }


//    @Test
//    void testFindDiscountDetailsByProductId() {
//        List<Object[]> mockResult = Arrays.asList(
//                new Object[]{1, 10, BigDecimal.valueOf(100), 0, LocalDate.now().plusDays(5)},
//                new Object[]{2, 5, BigDecimal.valueOf(150), 10, LocalDate.now().plusDays(10)}
//        );
//
//        when(invoiceItemRepository.findDiscountDetailsByProductId(1))
//                .thenReturn(mockResult);
//
//        List<Object[]> result = invoiceItemRepository.findDiscountDetailsByProductId(1);
//
//        assertFalse(result.isEmpty());
//        assertEquals(BigDecimal.valueOf(100), result.get(0)[2]); // Unit price
//    }


    @Test
    void testFindAllByExpirationDateBetween() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(10);

        when(invoiceItemRepository.findAllByExpirationDateBetween(startDate, endDate))
                .thenReturn(List.of(invoiceItem));

        List<InvoiceItem> result = invoiceItemRepository.findAllByExpirationDateBetween(startDate, endDate);

        assertFalse(result.isEmpty());
        assertEquals(10, result.get(0).getQuantity());
        assertEquals(product, result.get(0).getProduct());  // Verify the product object is set correctly
    }
}
