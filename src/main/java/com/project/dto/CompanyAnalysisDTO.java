package com.project.dto;

import java.math.BigDecimal;

public class CompanyAnalysisDTO {
    private Integer companyId;
    private String companyName;
    private BigDecimal totalPrice;
    private int month;
    private int year;

    public CompanyAnalysisDTO(Integer companyId, String companyName, BigDecimal totalPrice, int month, int year) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.totalPrice = totalPrice;
        this.month = month;
        this.year = year;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPurchase) {
        this.totalPrice = totalPurchase;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
