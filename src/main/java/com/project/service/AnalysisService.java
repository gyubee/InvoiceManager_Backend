package com.project.service;

import com.project.dto.CategoryAnalysisDTO;
import com.project.repository.AnalysisRepository;
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
}
