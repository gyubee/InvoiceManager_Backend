package com.project.repository;

import com.project.dto.ItemDiscountDTO;
import com.project.entity.Discount;
import com.project.entity.InvoiceItem;
import com.project.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
        invoiceItem.setProduct(product);
        invoiceItem.setExpirationDate(LocalDate.now().plusDays(5));
        invoiceItem.setQuantity(10);
        invoiceItem.setUnitPrice(BigDecimal.valueOf(100));
    }


    @Test
    void testFindDiscountDetailsByProductId() {
        // Given
        Integer productId = 1;
        Discount discount = new Discount();
        discount.setDiscountRate(0.1f);

        ItemDiscountDTO itemDiscountDTO = new ItemDiscountDTO(
                invoiceItem.getInvoiceItemId(),
                discount.getDiscountRate(),
                invoiceItem.getQuantity(),
                invoiceItem.getUnitPrice(),
                invoiceItem.getExpirationDate()
        );

        List<ItemDiscountDTO> expectedDiscountDetails = new ArrayList<>();
        expectedDiscountDetails.add(itemDiscountDTO);

        when(invoiceItemRepository.findDiscountDetailsByProductId(productId))
                .thenReturn(expectedDiscountDetails);

        // When
        List<ItemDiscountDTO> actualDiscountDetails = invoiceItemRepository.findDiscountDetailsByProductId(productId);

        // Then
        assertEquals(expectedDiscountDetails.size(), actualDiscountDetails.size());
        assertEquals(expectedDiscountDetails.get(0).getInvoiceItemId(), actualDiscountDetails.get(0).getInvoiceItemId());
        assertEquals(expectedDiscountDetails.get(0).getDiscountRate(), actualDiscountDetails.get(0).getDiscountRate());
        assertEquals(expectedDiscountDetails.get(0).getQuantity(), actualDiscountDetails.get(0).getQuantity());
        assertEquals(expectedDiscountDetails.get(0).getUnitPrice(), actualDiscountDetails.get(0).getUnitPrice());
        assertEquals(expectedDiscountDetails.get(0).getExpirationDate(), actualDiscountDetails.get(0).getExpirationDate());
    }


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
