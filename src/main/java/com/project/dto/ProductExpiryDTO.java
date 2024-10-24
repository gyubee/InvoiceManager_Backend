package com.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductExpiryDTO {
    private Integer productId;
    private String productName;
    @JsonProperty("hscode")
    private String hscode;
    private BigDecimal price;
    private String categoryName;
    private String supplierName;
    private LocalDate closestExpiryDate;
    private Integer quantityAtClosestExpiryDate;
    private Integer totalStock;

    // Getters and setters
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public LocalDate getClosestExpiryDate() {
        return closestExpiryDate;
    }

    public void setClosestExpiryDate(LocalDate closestExpiryDate) {
        this.closestExpiryDate = closestExpiryDate;
    }

    public Integer getQuantityAtClosestExpiryDate() {
        return quantityAtClosestExpiryDate;
    }

    public void setQuantityAtClosestExpiryDate(Integer quantityAtClosestExpiryDate) {
        this.quantityAtClosestExpiryDate = quantityAtClosestExpiryDate;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
    }
}