package com.project.service;

import com.project.entity.Discount;
import com.project.entity.InvoiceItem;
import com.project.entity.Product;
import com.project.repository.DiscountRepository;
import com.project.repository.InvoiceItemRepository;
import com.project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Autowired
    private ProductRepository productRepository;

    // Insert new discount for item
    @Transactional
    public void applyDiscountToInvoiceItem(Integer productId, Integer invoiceItemId, Float discountRate) {

        InvoiceItem invoiceItem = invoiceItemRepository.findById(invoiceItemId)
                .orElseThrow(() -> new IllegalArgumentException("InvoiceItem not found with id: " + invoiceItemId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = invoiceItem.getExpirationDate();
        long daysBeforeExpiration = ChronoUnit.DAYS.between(currentDate, expirationDate);

        // Create and save Discount
        Discount discount = new Discount();
        discount.setDiscountRate(discountRate);
        discount.setProduct(product);
        discount.setInvoiceItem(invoiceItem);
        discount.setDaysBeforeExpiration((int) daysBeforeExpiration);
        discount.setDiscountStartDate(currentDate);
        discount.setDiscountEndDate(expirationDate);

        discountRepository.save(discount);
    }

    // Insert new discount for all products with certain daysLeftTillExpire
    @Transactional
    public void applyGlobalDiscountBasedOnExpiration(Integer daysLeftTillExpire, Float discountRate) {
        LocalDate currentDate = LocalDate.now();

        // Fetch all InvoiceItems where the expiration date is within the specified range
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findAllByExpirationDateBetween(
                currentDate,
                currentDate.plusDays(daysLeftTillExpire)
        );

        for (InvoiceItem invoiceItem : invoiceItems) {
            // Calculate the days until expiration
            Integer daysBeforeExpiration = Math.toIntExact(ChronoUnit.DAYS.between(currentDate, invoiceItem.getExpirationDate()));

            // Fetch the applicable discount
            Optional<Discount> applicableDiscount = discountRepository.findTopByProductProductIdAndDaysBeforeExpirationLessThanEqualOrderByDaysBeforeExpirationDesc(
                    invoiceItem.getProduct().getProductId(), daysBeforeExpiration);

            if (applicableDiscount.isPresent()) {
                Discount discount = applicableDiscount.get();
                // Create and save Discount for the invoice item
                Discount newDiscount = new Discount();
                newDiscount.setDiscountRate(discount.getDiscountRate());
                newDiscount.setProduct(invoiceItem.getProduct());
                newDiscount.setInvoiceItem(invoiceItem);
                newDiscount.setDaysBeforeExpiration((int) daysBeforeExpiration);
                newDiscount.setDiscountStartDate(currentDate);
                newDiscount.setDiscountEndDate(invoiceItem.getExpirationDate());

                discountRepository.save(newDiscount);
            }
        }
    }

}
