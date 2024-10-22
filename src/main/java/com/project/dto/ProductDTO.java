package com.project.dto;

import java.math.BigDecimal;

public class ProductDTO {
    private Integer productId;
    private String productName;
    private String hscode;
    private BigDecimal unitPrice;
    private BigDecimal quantity;

    // Constructors
    public ProductDTO() {}

    public ProductDTO(Integer productId, String productName, String hscode, BigDecimal unitPrice, BigDecimal quantity) {
        this.productId = productId;
        this.productName = productName;
        this.hscode = hscode;
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

    public String getHscode() {
        return hscode;
    }

    public void setHscode(String hscode) {
        this.hscode = hscode;
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