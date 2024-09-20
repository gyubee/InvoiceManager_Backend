package com.project.repository;

import com.project.dto.ItemDiscountDTO;
import com.project.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Integer> {

    // Details for setting discount for product items
    @Query("SELECT new com.project.dto.ItemDiscountDTO(ii.invoiceItemId, d.discountRate, ii.quantity, ii.unitPrice, ii.expirationDate) " +
            "FROM InvoiceItem ii " +
            "JOIN ii.product p " +
            "JOIN Discount d ON d.product.productId = p.productId " +
            "WHERE p.productId = :productId")
    List<ItemDiscountDTO> findDiscountDetailsByProductId(@Param("productId") Integer productId);

    //    @Query("SELECT ii.invoiceitemId, ii.quantity, p.salePrice, " +
//            "COALESCE(d.discountRate, 0) as discountRate, ii.expirationDate " +
//            "FROM InvoiceItem ii " +
//            "JOIN Product p ON ii.productId = p.productId " +
//            "LEFT JOIN Discount d ON (d.productId = ii.productId) " +
//            "WHERE ii.productId = :productId " +
//            "AND ii.expirationDate >= CURRENT_DATE " +
//            "ORDER BY ii.expirationDate ASC")
//    List<Object[]> findDiscountDetailsByProductId(Integer productId);

    List<InvoiceItem> findAllByExpirationDateBetween(LocalDate startDate, LocalDate endDate);

//    bestSeller method
    @Query("SELECT i.product.productName, SUM(i.quantity) as totalQuantity " +
            "FROM InvoiceItem i " +
            "GROUP BY i.product.productName " +
            "ORDER BY totalQuantity DESC " +
            "LIMIT :limit")
    List<Object[]> findTopSellingProducts(@Param("limit") int limit);


}
