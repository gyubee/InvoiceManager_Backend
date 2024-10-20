package com.project.repository;

import com.project.dto.CategoryAnalysisDTO;
import com.project.dto.CompanyAnalysisDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@DataJpaTest
public class AnalysisRepositoryTest {

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private CompanyAnalysisRepository companyAnalysisRepository;

    @Test
    public void testFindAllCompanyAnalysis() {
        // List<CompanyAnalysisDTO> result = companyAnalysisRepository.findAllCompanyPurchasesGroupedByMonth();

        List<CategoryAnalysisDTO> result = analysisRepository.findAllCategoryProductSales();

        System.out.println(result);
    }
}
