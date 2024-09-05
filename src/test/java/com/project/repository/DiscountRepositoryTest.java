package com.project.repository;

import com.project.entity.Discount;
import com.project.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DiscountRepositoryTest {

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private Discount discount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Product product = new Product();
        product.setProductId(1);

        discount = new Discount();
        discount.setProduct(product);
        discount.setDiscountRate(10.0f);
        discount.setDaysBeforeExpiration(5);
    }

    @Test
    void testFindTopByProductProductIdAndDaysBeforeExpirationLessThanEqualOrderByDaysBeforeExpirationDesc() {
        when(discountRepository.findTopByProductProductIdAndDaysBeforeExpirationLessThanEqualOrderByDaysBeforeExpirationDesc(1, 5))
                .thenReturn(Optional.of(discount));

        Optional<Discount> result = discountRepository.findTopByProductProductIdAndDaysBeforeExpirationLessThanEqualOrderByDaysBeforeExpirationDesc(1, 5);

        assertTrue(result.isPresent());
        assertEquals(10.0f, result.get().getDiscountRate());
    }
}
