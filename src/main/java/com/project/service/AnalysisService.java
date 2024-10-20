package com.project.service;

import com.project.dto.CategoryAnalysisDTO;
import com.project.dto.CompanyAnalysisDTO;
import com.project.dto.MonthlySalesAnalysisDTO;
import com.project.repository.AnalysisRepository;
//import com.project.repository.CompanyAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalysisService {
    private final AnalysisRepository analysisRepository;

    @Autowired
    public AnalysisService(AnalysisRepository analysisRepository){
        this.analysisRepository = analysisRepository;
    }

    public List<CategoryAnalysisDTO> getAllCategoryProductSales(){
        return analysisRepository.findAllCategoryProductSales();
    }

    public List<CompanyAnalysisDTO> getAllCompanyAnalysis(){
        return analysisRepository.findAllCompanyPurchasesGroupedByMonth();
    }

    public List<MonthlySalesAnalysisDTO> getMonthlySalesComparison() {
        return analysisRepository.findMonthlySalesComparison();
    }
}
