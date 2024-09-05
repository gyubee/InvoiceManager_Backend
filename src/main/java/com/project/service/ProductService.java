package com.project.service;

import com.project.dto.ProductExpiryDTO;
import com.project.entity.Product;
import com.project.repository.InvoiceItemRepository;
import com.project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get Product by productId
    public Optional<Product> getProductById(Integer productId) {
        return productRepository.findById(productId);
    }



    // STOCK related methods
    // Get all products sorted by stock (ascending)
    public List<Product> getAllProductsSortedByStock() {
        return productRepository.findAllByOrderByStockAsc();
    }

    // Get all products with stock > 0
    public List<Product> getAllProductsWithStock() {
        return productRepository.findAllWithStock();
    }

    // Get the stock level of a specific product by ID
    public Integer getStockByProductId(Integer productId) {
        return productRepository.findStockByProductId(productId);
    }

    // Update stock with new value for a product
    public Product updateProductStock(Integer productId, Integer newStock) {

        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            product.setStock(newStock);

            return productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
    }



    // DISCOUNT related methods
    // All product with the earliest expiration date first
    public List<ProductExpiryDTO> getAllProductsWithEarliestExpiry() {
        List<Object[]> results = productRepository.findAllProductsWithEarliestExpirationDateSorted();
        List<ProductExpiryDTO> dtoList = new ArrayList<>();

        for (Object[] result : results) {
            Integer productId = (Integer) result[0];
            String productName = (String) result[1];
            String hsCode = (String) result[2];
            BigDecimal price = (BigDecimal) result[3];
            String categoryName = (String) result[4];
            String supplierName = (String) result[5];
            LocalDate closestExpiryDate = ((java.sql.Date) result[6]).toLocalDate();
            Integer quantityAtClosestExpiryDate = ((Number) result[7]).intValue();
            Integer totalStock = ((Number) result[8]).intValue();

            ProductExpiryDTO dto = new ProductExpiryDTO();
            dto.setProductId(productId);
            dto.setProductName(productName);
            dto.setHsCode(hsCode);
            dto.setPrice(price);
            dto.setCategoryName(categoryName);
            dto.setSupplierName(supplierName);
            dto.setClosestExpiryDate(closestExpiryDate);
            dto.setQuantityAtClosestExpiryDate(quantityAtClosestExpiryDate);
            dto.setTotalStock(totalStock);

            dtoList.add(dto);
        }

        return dtoList;
    }

}
