package com.project.repository;

import com.project.entity.Company;
import com.project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByProductNameAndHscodeAndSupplier(String productName, String hsCode, Company company);

    // STOCK related methods
    //Products sorted by stock
    List<Product> findAllByOrderByStockAsc();

    // Product with stocks available
    @Query("SELECT p FROM Product p WHERE p.stock > 0")
    List<Product> findAllWithStock();

    // Retrieve stock from productId
    @Query("SELECT p.stock FROM Product p WHERE p.productId = :productId")
    Integer findStockByProductId(Integer productId);



    // DISCOUNT related methods
    // All product with the earliest expiration date first
    @Query("SELECT p.productId, p.productName, p.hscode, p.salePrice, c.categoryName, s.companyName as supplierName, " +
            "MIN(ii.expirationDate) as closestExpiryDate, " +
            "SUM(CASE WHEN ii.expirationDate = (SELECT MIN(ii2.expirationDate) FROM InvoiceItem ii2 WHERE ii2.product.productId = p.productId AND ii2.expirationDate >= CURRENT_DATE) THEN ii.quantity ELSE 0 END) as quantityAtClosestExpiryDate, " +
            "SUM(ii.quantity) as totalStock " +
            "FROM Product p " +
            "JOIN InvoiceItem ii ON p.productId = ii.product.productId " +
            "JOIN p.category c " +
            "JOIN p.supplier s " +
            "WHERE ii.expirationDate >= CURRENT_DATE OR ii.expirationDate IS NULL " +
            "GROUP BY p.productId, p.productName, p.hscode, p.salePrice, c.categoryName, s.companyName " +
            "ORDER BY " +
            "CASE WHEN MIN(ii.expirationDate) IS NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN MIN(ii.expirationDate) IS NULL THEN SUM(ii.quantity) ELSE MIN(ii.expirationDate) END ASC")
    List<Object[]> findAllProductsWithEarliestExpirationDateSorted();

    //    @Query("SELECT p.productId, p.productName, p.hsCode, p.price, c.categoryName, s.companyName as supplierName, " +
//            "MIN(ii.expirationDate) as closestExpiryDate, " +
//            "SUM(CASE WHEN ii.expirationDate = MIN(ii.expirationDate) THEN ii.quantity ELSE 0 END) as quantityAtClosestExpiryDate, " +
//            "SUM(ii.quantity) as totalStock " +
//            "FROM Product p " +
//            "JOIN InvoiceItem ii ON p.productId = ii.productId " +
//            "JOIN Category c ON p.categoryId = c.categoryId " +
//            "JOIN Company s ON p.supplierId = s.companyId " +
//            "WHERE ii.expirationDate >= CURRENT_DATE " +
//            "GROUP BY p.productId, p.productName, p.hsCode, p.price, c.categoryName, s.companyName " +
//            "HAVING SUM(CASE WHEN ii.expirationDate = MIN(ii.expirationDate) THEN ii.quantity ELSE 0 END) > 0 " +
//            "ORDER BY closestExpiryDate ASC")
//    List<Object[]> findAllProductsWithEarliestExpirationDateSorted(); // assume that quantity in InvoiceItem is updated realtime as products are sold


}