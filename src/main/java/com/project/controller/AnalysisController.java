package com.project.controller;

import com.project.dto.CategoryAnalysisDTO;
import com.project.dto.CompanyAnalysisDTO;
import com.project.service.AnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/invoicemanager/anaylsis")
public class AnalysisController {
    private final AnalysisService analysisService;

    @Autowired
    public AnalysisController(AnalysisService analysisService){
        this.analysisService = analysisService;
    }

    @Operation(summary = "Get all products with category and sales by month", description = "Retrieve all product names with their category name and sales of each month")
    @GetMapping("/category")
    public ResponseEntity<List<CategoryAnalysisDTO>> getAllCategoryAnalysis(){
        List<CategoryAnalysisDTO> categoryAnalysis = analysisService.getAllCategoryProductSales();
        return ResponseEntity.ok(categoryAnalysis);
    }

//    @GetMapping("/company")
//    public ResponseEntity<List<CompanyAnalysisDTO>> getAllCompanyAnalysis(){
//        List<CompanyAnalysisDTO> companyAnalysis = analysisService.getAllCompanyAnalysis();
//        return ResponseEntity.ok(companyAnalysis);
//    }

}
