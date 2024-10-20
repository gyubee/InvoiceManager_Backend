package com.project.dto;

import java.time.LocalDateTime;

public class CategoryAnalysisDTO {

    private Integer productId;
    private String productName;
    private Integer categoryId;
    private String categoryName;
    private LocalDateTime salesMonth;
    private Integer monthlySales;


    public CategoryAnalysisDTO(Integer productId, String productName, Integer categoryId, String categoryName, LocalDateTime salesMonth, Integer monthlySales) {
        this.productId = productId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.salesMonth = salesMonth;
        this.monthlySales = monthlySales;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public LocalDateTime getSalesMonth() {
        return salesMonth;
    }

    public void setSalesMonth(LocalDateTime salesMonth) {
        this.salesMonth = salesMonth;
    }

    public Integer getMonthlySales() {
        return monthlySales;
    }

    public void setMonthlySales(Integer monthlySales) {
        this.monthlySales = monthlySales;
    }
}
