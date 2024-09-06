//invoiceService
package com.project.service;

import com.project.entity.*;
import com.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.math.BigDecimal;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* INVOICE LOGIC
1. Company name, Company email 값을 받아와서 company table에서 해당 데이터가 있는지 확인.
        1-1. 있다면 일치하는 companyId를 받아서 invoice table에 receive_date, total_price, company_id를 넣어서 저장
        1-2. 없다면 인보이스 추출 데이터에서 Company name, Company email, Company address를 받아서 company 테이블에 저장 후 1-1 진행
2. 인보이스 추출 데이터 중 product_name, hscode, company_id 정보를 가지고 product테이블에서 해당 데이터가 있는지 확인
        2-1. 있다면 일치하는 productId를 받아와서 invoiceItem에 (추출 데이터: quantity, unit_price, expiration_date(nullable), 따로 받아올 데이터: invoice_id, product_id) 저장
        2-2. 없다면 product에 저장하게 모달을 띄워서 sale price, categoryId 입력받게 하고(일단은 임의 값 -1 넣기), product_name, hscode, sale price, categoryId, stock, supplierId 입력해서 product 생성 후 해당 productId 받아와서 2-1 진행
 */

@Service
public class InvoiceService {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);

    @Transactional
    public void saveInvoiceData(Map<String, Object> invoiceData) {
        Company company = setCompany(invoiceData);

        Invoice invoice = setInvoice(invoiceData, company);
        invoice = invoiceRepository.save(invoice);

        List<Map<String, Object>> products = (List<Map<String, Object>>) invoiceData.get("products");
        for (Map<String, Object> productData : products) {
            Product product = setProduct(productData, company);
            setInvoiceItem(invoice, product, productData);
        }

    }

    private Company setCompany(Map<String, Object> invoiceData) {
        String companyName = (String) invoiceData.get("company_name");
        String companyEmail = (String) invoiceData.get("company_email"); // assume that all invoices has company_email

        Company company = companyRepository.findByCompanyNameAndCompanyEmail(companyName, companyEmail)
                .orElseGet(() -> {
                    Company newCompany = new Company();
                    newCompany.setCompanyName(companyName);
                    newCompany.setCompanyEmail(companyEmail);
                    newCompany.setCompanyAddress((String) invoiceData.get("company_address"));
                    return companyRepository.save(newCompany);
                });
        return company;
    }

    private Invoice setInvoice(Map<String, Object> invoiceData, Company company) {
        Invoice invoice = new Invoice();
        invoice.setCompany(company);

        try {
            String dateString = (String) invoiceData.get("receive_date");
            LocalDate receiveDate = LocalDate.parse(dateString, DATE_FORMATTER);
            invoice.setReceiveDate(receiveDate);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format. Expected 'dd MMM yyyy', got: " + invoiceData.get("order_date"), e);
        }

        invoice.setTotalPrice(new BigDecimal(((String) invoiceData.get("total_price")).replace(",", "")));
        return invoice;
    }

    private Product setProduct(Map<String, Object> productData, Company supplier) {
        String productName = (String) productData.get("product_name");
        String hsCode = (String) productData.get("hs_code");
        Integer quantity = (Integer) productData.get("quantity");

        Product product = productRepository.findByProductNameAndHscodeAndSupplier(productName, hsCode, supplier)
                .orElseGet(() -> {
                    Product newProduct = new Product();
                    newProduct.setProductName(productName);
                    newProduct.setHscode(hsCode);
                    newProduct.setSalePrice(new BigDecimal(-1)); // value needs to be treated by admin later on
                    newProduct.setSupplier(supplier);

                    Category category = setCategory("Default");
                    newProduct.setCategory(category);

                    newProduct.setStock(quantity);

                    return productRepository.save(newProduct);
                });

        if (product.getProductId() != null) {
            product.setStock(product.getStock() + quantity);
            productRepository.save(product);
        }

        return product;
    }

    private Category setCategory(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setCategoryName(categoryName);
                    return categoryRepository.save(newCategory);
                });
    }

    private void setInvoiceItem(Invoice invoice, Product product, Map<String, Object> productData) {
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoice(invoice);
        invoiceItem.setProduct(product);
        invoiceItem.setQuantity((Integer) productData.get("quantity"));
        invoiceItem.setUnitPrice(new BigDecimal((String) productData.get("unit_price")));

        // expiration_date nullable
        if (productData.containsKey("expiration_date") && productData.get("expiration_date") != null) {
            String expirationDateString = (String) productData.get("expiration_date");
            try {
                LocalDate expirationDate = LocalDate.parse(expirationDateString, DATE_FORMATTER);
                invoiceItem.setExpirationDate(expirationDate);
            } catch (DateTimeParseException e) {
                throw new RuntimeException("Invalid date format for expiration_date. Expected 'dd MMM yyyy', got: " + expirationDateString, e);
            }
        }

        invoiceItemRepository.save(invoiceItem);
    }

//    private static final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
//            DateTimeFormatter.ofPattern("MMM-dd-yyyy"),
//            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
//            DateTimeFormatter.ofPattern("yyyy-MM-dd")
//    );
//
//    private BigDecimal getBigDecimalValue(Map<String, Object> data, String key) {
//        Object value = data.get(key);
//        if (value == null) {
//            logger.warn("Missing value for key: {}", key);
//            return BigDecimal.ZERO;
//        }
//        try {
//            if (value instanceof Number) {
//                return new BigDecimal(value.toString());
//            } else if (value instanceof String) {
//                return new BigDecimal((String) value);
//            } else {
//                logger.warn("Unexpected type for key {}: {}", key, value.getClass());
//                return BigDecimal.ZERO;
//            }
//        } catch (NumberFormatException e) {
//            logger.error("Invalid number format for key: {}", key, e);
//            return BigDecimal.ZERO;
//        }
//    }
//
//    private LocalDate parseDateValue(Map<String, Object> data, String key) {
//        String dateString = getStringValue(data, key);
//        if (dateString.isEmpty()) {
//            logger.warn("Empty date string for key: {}", key);
//            return LocalDate.now();
//        }
//        try {
//            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MMM-dd-yyyy"));
//        } catch (DateTimeParseException e) {
//            logger.error("Unable to parse date: {} for key: {}", dateString, key, e);
//            return LocalDate.now();
//        }
//    }
//
//    private String getStringValue(Map<String, Object> data, String key) {
//        Object value = data.get(key);
//        if (value == null) {
//            logger.warn("Missing value for key: {}", key);
//            return "";
//        }
//        return value.toString();
//    }

}