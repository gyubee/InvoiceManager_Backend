package com.project.repository;

import com.project.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Integer> {

//    category
    @Query("SELECT p.category.categoryId, SUM(s.monthlySales) " +
            "FROM Sales s JOIN s.product p " +
            "WHERE s.salesMonth = :salesMonth " +
            "GROUP BY p.category.categoryId")
    List<Object[]> getSalesByCategoryAndMonth(@Param("salesMonth") LocalDateTime salesMonth);

//monthly revenue
    @Query("SELECT COALESCE(SUM(i.totalPrice), 0) FROM Invoice i WHERE YEAR(i.receiveDate) = :year AND MONTH(i.receiveDate) = :month")
    BigDecimal calculateTotalRevenueForMonth(@Param("year") int year, @Param("month") int month);


    //    bestSeller method
    @Query("SELECT i.product.productName, SUM(i.quantity) as totalQuantity " +
            "FROM InvoiceItem i " +
            "GROUP BY i.product.productName " +
            "ORDER BY totalQuantity DESC " +
            "LIMIT :limit")
    List<Object[]> findTopSellingProducts(@Param("limit") int limit);



    // positive trending
    @Query("SELECT s.product.productId, s.monthlySales " +
            "FROM Sales s WHERE s.salesMonth = :salesMonth")
    List<Object[]> findSalesByMonth(@Param("salesMonth") LocalDateTime salesMonth);


}
