package com.project.repository;

import com.project.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Integer> {

    // Details for setting discount for product items
    @Query("SELECT ii.invoiceitemId, ii.quantity, p.salePrice, " +
            "COALESCE(d.discountRate, 0) as discountRate, ii.expirationDate " +
            "FROM InvoiceItem ii " +
            "JOIN Product p ON ii.productId = p.productId " +
            "LEFT JOIN Discount d ON (d.productId = ii.productId) " +
            "WHERE ii.productId = :productId " +
            "AND ii.expirationDate >= CURRENT_DATE " +
            "ORDER BY ii.expirationDate ASC")
    List<Object[]> findDiscountDetailsByProductId(Integer productId);

    List<InvoiceItem> findAllByExpirationDateBetween(LocalDate startDate, LocalDate endDate);

}
