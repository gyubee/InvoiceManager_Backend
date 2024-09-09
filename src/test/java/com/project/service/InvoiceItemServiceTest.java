package com.project.service;

import com.project.dto.ItemDiscountDTO;
import com.project.entity.Discount;
import com.project.entity.InvoiceItem;
import com.project.entity.Product;
import com.project.repository.InvoiceItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class InvoiceItemServiceTest {

    @Mock
    private InvoiceItemService invoiceItemService;

    @InjectMocks
    private InvoiceItem invoiceItem;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setProductId(1);

        invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceItemId(1);
        invoiceItem.setProduct(product);
        invoiceItem.setQuantity(5);
        invoiceItem.setUnitPrice(BigDecimal.valueOf(100));
        invoiceItem.setExpirationDate(LocalDate.now().plusDays(30));
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

        when(invoiceItemService.findDiscountDetailsByProductId(productId))
                .thenReturn(expectedDiscountDetails);

        // When
        List<ItemDiscountDTO> actualDiscountDetails = invoiceItemService.findDiscountDetailsByProductId(productId);

        // Then
        assertEquals(expectedDiscountDetails.size(), actualDiscountDetails.size());
        assertEquals(expectedDiscountDetails.get(0).getInvoiceItemId(), actualDiscountDetails.get(0).getInvoiceItemId());
        assertEquals(expectedDiscountDetails.get(0).getDiscountRate(), actualDiscountDetails.get(0).getDiscountRate());
        assertEquals(expectedDiscountDetails.get(0).getQuantity(), actualDiscountDetails.get(0).getQuantity());
        assertEquals(expectedDiscountDetails.get(0).getUnitPrice(), actualDiscountDetails.get(0).getUnitPrice());
        assertEquals(expectedDiscountDetails.get(0).getExpirationDate(), actualDiscountDetails.get(0).getExpirationDate());
    }
}
