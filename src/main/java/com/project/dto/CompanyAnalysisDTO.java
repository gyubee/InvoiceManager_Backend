package com.project.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CompanyAnalysisDTO {
    private Integer companyId;
    private String companyName;
    private BigDecimal totalPrice;
    private String receiveMonth;

    public CompanyAnalysisDTO(Integer companyId, String companyName, BigDecimal totalPrice, String receiveMonth) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.totalPrice = totalPrice;
        this.receiveMonth = receiveMonth;
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

    public String getReceiveMonth() {
        return receiveMonth;
    }

    public void setReceiveMonth(String receiveMonth) {
        this.receiveMonth = receiveMonth;
    }
}
