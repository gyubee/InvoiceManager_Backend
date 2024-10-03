package com.project.repository;

import com.project.entity.Sales;
import com.project.dto.CategoryAnalysisDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnalysisRepository extends JpaRepository<Sales, Integer> {

    @Query("SELECT new com.project.dto.CategoryAnalysisDTO(p.productId, p.productName, c.categoryId, c.categoryName, s.salesMonth, s.monthlySales) " +
            "FROM Sales s " +
            "JOIN s.product p " +
            "JOIN p.category c")
    List<CategoryAnalysisDTO> findAllCategoryProductSales();

}