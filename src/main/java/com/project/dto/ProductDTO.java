package com.project.dto;

import java.math.BigDecimal;

public class ProductDTO {
    private Integer productId;
    private String productName;
    private String hsCode;
    private BigDecimal unitPrice;
    private BigDecimal quantity;

    // Constructors
    public ProductDTO() {}

    public ProductDTO(Integer productId, String productName, String hsCode, BigDecimal unitPrice, BigDecimal quantity) {
        this.productId = productId;
        this.productName = productName;
        this.hsCode = hsCode;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    // Getters and Setters
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

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}