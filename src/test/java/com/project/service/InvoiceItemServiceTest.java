package com.project.service;

import com.project.dto.ItemDiscountDTO;
import com.project.repository.InvoiceItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvoiceItemServiceTest {

    @Mock
    private InvoiceItemRepository invoiceItemRepository;

    @InjectMocks
    private InvoiceItemService invoiceItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindDiscountDetailsByProductId() {
        List<Object[]> mockResult = List.of(
                new Object[]{
                        1, // invoiceItemId
                        10, // quantity
                        BigDecimal.valueOf(100), // unitPrice
                        0, // discountRate
                        LocalDate.now().plusDays(5) // expirationDate
                },
                new Object[]{
                        2, // invoiceItemId
                        5, // quantity
                        BigDecimal.valueOf(150), // unitPrice
                        10, // discountRate
                        LocalDate.now().plusDays(10) // expirationDate
                },
                new Object[]{
                        3, // invoiceItemId
                        8, // quantity
                        BigDecimal.valueOf(200), // unitPrice
                        20, // discountRate
                        LocalDate.now().plusDays(15) // expirationDate
                }
        );

        when(invoiceItemRepository.findDiscountDetailsByProductId(1))
                .thenReturn(mockResult);

        List<ItemDiscountDTO> result = invoiceItemService.findDiscountDetailsByProductId(1);

        // Assertions
        assertFalse(result.isEmpty());
        assertEquals(3, result.size());

        assertEquals(1, result.get(0).getInvoiceItemId());
        assertEquals(10, result.get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(100), result.get(0).getUnitPrice());
        assertEquals(0, result.get(0).getDiscountRate());
        assertEquals(LocalDate.now().plusDays(5), result.get(0).getExpirationDate());

        assertEquals(2, result.get(1).getInvoiceItemId());
        assertEquals(5, result.get(1).getQuantity());
        assertEquals(BigDecimal.valueOf(150), result.get(1).getUnitPrice());
        assertEquals(10, result.get(1).getDiscountRate());
        assertEquals(LocalDate.now().plusDays(10), result.get(1).getExpirationDate());

        assertEquals(3, result.get(2).getInvoiceItemId());
        assertEquals(8, result.get(2).getQuantity());
        assertEquals(BigDecimal.valueOf(200), result.get(2).getUnitPrice());
        assertEquals(20, result.get(2).getDiscountRate());
        assertEquals(LocalDate.now().plusDays(15), result.get(2).getExpirationDate());
    }
}