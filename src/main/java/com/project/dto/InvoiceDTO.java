package com.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class InvoiceDTO {
    private Integer invoiceId;
    private String companyName;
    private String country;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;

    private BigDecimal totalPrice;
    private List<ProductDTO> products;

    // Constructors
    public InvoiceDTO() {}

    public InvoiceDTO(Integer invoiceId, String companyName, String country, LocalDate orderDate, BigDecimal totalPrice, List<ProductDTO> products) {
        this.invoiceId = invoiceId;
        this.companyName = companyName;
        this.country = country;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.products = products;
    }

    // Getters and Setters
    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}