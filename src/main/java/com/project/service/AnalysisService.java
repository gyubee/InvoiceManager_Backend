package com.project.service;

import com.project.dto.CategoryAnalysisDTO;
import com.project.dto.CompanyAnalysisDTO;
import com.project.repository.AnalysisRepository;
import com.project.repository.CompanyAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalysisService {
    private final AnalysisRepository analysisRepository;
    private final CompanyAnalysisRepository companyAnalysisRepository;

    @Autowired
    public AnalysisService(AnalysisRepository analysisRepository, CompanyAnalysisRepository companyAnalysisRepository){
        this.analysisRepository = analysisRepository;
        this.companyAnalysisRepository = companyAnalysisRepository;
    }

    public List<CategoryAnalysisDTO> getAllCategoryProductSales(){
        return analysisRepository.findAllCategoryProductSales();
    }

//    public List<CompanyAnalysisDTO> getAllCompanyAnalysis(){
//        return companyAnalysisRepository.findAllCompanyPurchasesGroupedByMonth();
//    }
}
