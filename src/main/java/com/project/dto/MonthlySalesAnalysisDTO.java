package com.project.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MonthlySalesAnalysisDTO {
    private Integer productId;
    private String productName;
    private BigDecimal salePrice;
    private LocalDateTime salesMonth;
    private Integer monthlySales;

    public MonthlySalesAnalysisDTO(Integer productId, String productName, BigDecimal salePrice, LocalDateTime salesMonth, Integer monthlySales) {
        this.productId = productId;
        this.productName = productName;
        this.salePrice = salePrice;
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

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
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
