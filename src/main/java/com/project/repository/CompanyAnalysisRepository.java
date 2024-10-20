package com.project.repository;

import com.project.dto.CompanyAnalysisDTO;
import com.project.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyAnalysisRepository extends JpaRepository<Invoice, Integer> {

//    @Query("SELECT new com.project.dto.CompanyAnalysisDTO(c.companyId, c.companyName, SUM(i.totalPrice), " +
//            "FUNCTION('MONTH', i.receiveDate), FUNCTION('YEAR', i.receiveDate)) " +
//            "FROM Invoice i " +
//            "JOIN i.company c " +
//            "GROUP BY c.companyId, c.companyName, FUNCTION('MONTH', i.receiveDate), FUNCTION('YEAR', i.receiveDate)")
//    List<CompanyAnalysisDTO> findAllCompanyPurchasesGroupedByMonth();
}