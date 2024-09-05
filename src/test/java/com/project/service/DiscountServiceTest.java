package com.project.service;

import com.project.entity.Discount;
import com.project.entity.InvoiceItem;
import com.project.entity.Product;
import com.project.repository.DiscountRepository;
import com.project.repository.InvoiceItemRepository;
import com.project.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class DiscountServiceTest {

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private InvoiceItemRepository invoiceItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DiscountService discountService;

    private InvoiceItem invoiceItem;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setProductId(1);
        product.setSalePrice(BigDecimal.valueOf(100));

        invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceItemId(1);
        invoiceItem.setProduct(product);
        invoiceItem.setExpirationDate(LocalDate.now().plusDays(10));
    }

    @Test
    void testApplyDiscountToInvoiceItem() {
        when(invoiceItemRepository.findById(1)).thenReturn(Optional.of(invoiceItem));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        discountService.applyDiscountToInvoiceItem(1, 1, 10.0f);

        verify(discountRepository, times(1)).save(any(Discount.class));
    }

    @Test
    void testApplyGlobalDiscountBasedOnExpiration() {
        List<InvoiceItem> invoiceItems = List.of(invoiceItem);
        when(invoiceItemRepository.findAllByExpirationDateBetween(
                LocalDate.now(),
                LocalDate.now().plusDays(10)
        )).thenReturn(invoiceItems);

        Discount existingDiscount = new Discount();
        existingDiscount.setDiscountRate(10.0f);
        when(discountRepository.findTopByProductProductIdAndDaysBeforeExpirationLessThanEqualOrderByDaysBeforeExpirationDesc(
                1, 10
        )).thenReturn(Optional.of(existingDiscount));

        discountService.applyGlobalDiscountBasedOnExpiration(10, 10.0f);

        verify(discountRepository, times(1)).save(any(Discount.class));
    }
}
