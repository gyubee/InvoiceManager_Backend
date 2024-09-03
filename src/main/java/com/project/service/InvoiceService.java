//invoiceService
package com.project.service;

import com.project.dto.InvoiceDTO;
import com.project.dto.ProductDTO;
//import com.project.entity.Invoice;
//import com.project.entity.Product;
//import com.project.repository.InvoiceRepository;
//import com.project.repository.ProductRepository;
// is it okay to load like this?
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
import java.time.format.DateTimeFormatter;
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

    //    승우

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

//    data time fortmat
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

        invoiceRepository.save(invoice);
    }

    private Company setCompany(Map<String, Object> invoiceData) {
        String companyName = (String) invoiceData.get("company_name");
        Company company = companyRepository.findByCompanyName(companyName)
                .orElse(new Company());

        company.setCompanyName(companyName);
        company.setCompanyAddress((String) invoiceData.get("company_address_postcode"));
        return companyRepository.save(company);
    }

    private Invoice setInvoice(Map<String, Object> invoiceData, Company company) {
        Invoice invoice = new Invoice();
        invoice.setCompany(company);


        try {
            String dateString = (String) invoiceData.get("order_date");
            LocalDate orderDate = LocalDate.parse(dateString, DATE_FORMATTER);
            invoice.setReceiveDate(orderDate);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format. Expected 'dd MMM yyyy', got: " + invoiceData.get("order_date"), e);
        }

        invoice.setTotalPrice(new BigDecimal(((String) invoiceData.get("total_price")).replace(",", "")));
        invoice.setInvoiceItems(new ArrayList<>());
        return invoice;
    }

    private Product setProduct(Map<String, Object> productData, Company supplier) {
        String productName = (String) productData.get("product_name");
        Product product = productRepository.findByProductName(productName)
                .orElse(new Product());

        product.setProductName(productName);
        product.setHscode((String) productData.get("hs_code"));
        product.setSalePrice(new BigDecimal((String) productData.get("unit_price")));
        product.setSupplier(supplier);

        // Set a default category
        Category category = setCategory("Default");
        product.setCategory(category);

        // Set a default stock value
        product.setStock(0);

        return productRepository.save(product);
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

        invoice.getInvoiceItems().add(invoiceItem);
        invoiceItemRepository.save(invoiceItem);
    }
}