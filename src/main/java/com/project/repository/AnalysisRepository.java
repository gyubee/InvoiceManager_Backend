package com.project.repository;

import com.project.dto.CompanyAnalysisDTO;
import com.project.dto.MonthlySalesAnalysisDTO;
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

//    @Query("SELECT new com.project.dto.CompanyAnalysisDTO(c.companyId, c.companyName, SUM(i.totalPrice), " +
//            "FUNCTION('MONTH', i.receiveDate), FUNCTION('YEAR', i.receiveDate)) " +
//            "FROM Invoice i " +
//            "JOIN i.company c " +
//            "GROUP BY c.companyId, c.companyName, FUNCTION('MONTH', i.receiveDate), FUNCTION('YEAR', i.receiveDate)")
//    List<CompanyAnalysisDTO> findAllCompanyPurchasesGroupedByMonth();

    @Query("SELECT new com.project.dto.MonthlySalesAnalysisDTO(p.productId, p.productName, p.salePrice, s.salesMonth, s.monthlySales) " +
            "FROM Sales s " +
            "JOIN s.product p")
    List<MonthlySalesAnalysisDTO> findMonthlySalesComparison();
}