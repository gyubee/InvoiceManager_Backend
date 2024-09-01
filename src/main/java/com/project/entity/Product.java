//entity/Product
package com.project.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String hscode;

    @Column(nullable = false)
    private BigDecimal salePrice;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Company supplier;

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

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Company getSupplier() {
        return supplier;
    }

    public void setSupplier(Company supplier) {
        this.supplier = supplier;
    }

    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer productId;
//
//    @Column(name = "product_name", nullable = false)
//    private String productName;
//
//    @Column(name = "hs_code", nullable = false)
//    private String hsCode;
//
//    @Column(name = "unit_price", nullable = false)
//    private BigDecimal unitPrice;
//
//    @Column(name = "quantity", nullable = false)
//    private BigDecimal quantity;
//
////    @Column(name = "category")
////    private String category;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "invoice_id")
//    private Invoice invoice;



//    public Integer getProductId() {
//        return productId;
//    }
//
//    public void setProductId(Integer productId) {
//        this.productId = productId;
//    }
//
//    public String getProductName() {
//        return productName;
//    }
//
//    public void setProductName(String productName) {
//        this.productName = productName;
//    }

//    public String getHsCode() {
//        return hsCode;
//    }
//
//    public void setHsCode(String hsCode) {
//        this.hsCode = hsCode;
//    }
//    public BigDecimal getQuantity() {
//        return quantity;
//    }

//    public void setQuantity(BigDecimal quantity) {
//        this.quantity = quantity;
//    }
//
//    public BigDecimal getUnitPrice() {
//        return unitPrice;
//    }
//
//    public void setUnitPrice(BigDecimal price) {
//        this.unitPrice = price;
//    }

//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }

//    public Invoice getInvoice() {
//        return invoice;
//    }
//    public void setInvoice(Invoice invoice) {
//        this.invoice = invoice;
//    }
}

