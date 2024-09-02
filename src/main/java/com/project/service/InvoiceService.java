//invoiceService
package com.project.service;

import com.project.dto.InvoiceDTO;
import com.project.dto.ProductDTO;
import com.project.entity.Invoice;
import com.project.entity.Product;
import com.project.repository.InvoiceRepository;
import com.project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class InvoiceService {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProductRepository productRepository;

    private static final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
            DateTimeFormatter.ofPattern("MMM-dd-yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    );

//    @Transactional
//    public void saveInvoiceData(Map<String, Object> invoiceData) {
//        Invoice invoice = createInvoice(invoiceData);
//        Invoice savedInvoice = invoiceRepository.save(invoice);
//
//        List<Map<String, Object>> products = (List<Map<String, Object>>) invoiceData.get("products");
//        for (Map<String, Object> productData : products) {
//            saveOrUpdateProduct(productData, savedInvoice);
//        }
//    }

//    private Invoice createInvoice(Map<String, Object> invoiceData) {
//        Invoice invoice = new Invoice();
//        invoice.setCompanyName(getStringValue(invoiceData, "company_name"));
//        invoice.setCountry(getStringValue(invoiceData, "country"));
//        invoice.setOrderDate(parseDateValue(invoiceData, "order_date"));
//        invoice.setTotalPrice(getBigDecimalValue(invoiceData, "total_price"));
//        return invoice;
//    }

//    private void saveOrUpdateProduct(Map<String, Object> productData, Invoice invoice) {
//        Product product = new Product();
//        product.setProductName(getStringValue(productData, "product_name"));
//        product.setHsCode(getStringValue(productData, "hs_code"));
//        product.setQuantity(getBigDecimalValue(productData, "quantity"));
//        product.setUnitPrice(getBigDecimalValue(productData, "unit_price"));
//        product.setInvoice(invoice);
//
//        productRepository.save(product);
//    }


//    DTO
//    public List<InvoiceDTO> getAllInvoicesDTO() {
//        List<Invoice> invoices = invoiceRepository.findAll();
//        return invoices.stream().map(this::convertToDTO).collect(Collectors.toList());
//    }
//    public List<InvoiceDTO> getAllProductsDTO() {
//        List<Product> products = productRepository.findAll();
//        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
//    }

//    public InvoiceDTO getInvoiceWithProductsDTO(Long invoiceId) {
//        Invoice invoice = invoiceRepository.findById(invoiceId)
//                .orElseThrow(() -> new RuntimeException("Invoice not found"));
//        return convertToDTO(invoice);
//    }

//    private InvoiceDTO convertToDTO(Invoice invoice) {
//        InvoiceDTO dto = new InvoiceDTO();
//        dto.setInvoiceId(invoice.getInvoiceId());
//        dto.setCompanyName(invoice.getCompanyName());
//        dto.setCountry(invoice.getCountry());
//        dto.setOrderDate(invoice.getOrderDate());
//        dto.setTotalPrice(invoice.getTotalPrice());
//        dto.setProducts(invoice.getProducts().stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList()));
//        return dto;
//    }

//    private ProductDTO convertToDTO(Product product) {
//        ProductDTO dto = new ProductDTO();
//        dto.setProductId(product.getProductId());
//        dto.setProductName(product.getProductName());
//        dto.setHsCode(product.getHsCode());
//        dto.setUnitPrice(product.getUnitPrice());
//        dto.setQuantity(product.getQuantity());
//        return dto;
//    }


    private String getStringValue(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value == null) {
            logger.warn("Missing value for key: {}", key);
            return "";
        }
        return value.toString();
    }

    private BigDecimal getBigDecimalValue(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value == null) {
            logger.warn("Missing value for key: {}", key);
            return BigDecimal.ZERO;
        }
        try {
            if (value instanceof Number) {
                return new BigDecimal(value.toString());
            } else if (value instanceof String) {
                return new BigDecimal((String) value);
            } else {
                logger.warn("Unexpected type for key {}: {}", key, value.getClass());
                return BigDecimal.ZERO;
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid number format for key: {}", key, e);
            return BigDecimal.ZERO;
        }
    }

    private LocalDate parseDateValue(Map<String, Object> data, String key) {
        String dateString = getStringValue(data, key);
        if (dateString.isEmpty()) {
            logger.warn("Empty date string for key: {}", key);
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MMM-dd-yyyy"));
        } catch (DateTimeParseException e) {
            logger.error("Unable to parse date: {} for key: {}", dateString, key, e);
            return LocalDate.now();
        }
    }
}