package com.project.controller;

import com.project.dto.ItemDiscountDTO;
import com.project.dto.ProductExpiryDTO;
import com.project.entity.Product;
import com.project.service.DiscountService;
import com.project.service.InvoiceItemService;
import com.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/invoicemanager/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private InvoiceItemService invoiceItemService;

    @Autowired
    private DiscountService discountService;

    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }



    // STOCK
    @GetMapping("/stock")
    public List<Product> getAllProductsSortedByStock() {
        return productService.getAllProductsSortedByStock();
    }

    @GetMapping("/all/available")
    public List<Product> getAllProductsWithStock() {
        return productService.getAllProductsWithStock();
    }

    @GetMapping("/{id}/stock")
    public Integer getStockByProductId(@PathVariable Integer productId) {
        return productService.getStockByProductId(productId);
    }

    @PutMapping("/{id}/stock")
    public Product updateProductStock(@PathVariable Integer productId, @RequestParam Integer newStock) {
        return productService.updateProductStock(productId, newStock);
    }



    // DISCOUNT
    @GetMapping("/expiry")
    public List<ProductExpiryDTO> getAllProductsWithEarliestExpiry() {
        return productService.getAllProductsWithEarliestExpiry();
    }

    @GetMapping("/{productId}/expiry")
    public List<ItemDiscountDTO> findDiscountDetailsByProductId(@PathVariable Integer productId) {
        return invoiceItemService.findDiscountDetailsByProductId(productId);
    }

    @PostMapping("/{productId}/{invoiceItemId}/discounts/apply")
    public ResponseEntity<String> applyDiscount(
            @PathVariable Integer productId,
            @PathVariable Integer invoiceItemId,
            @RequestParam Float discountRate) {

        try {
            discountService.applyDiscountToInvoiceItem(productId, invoiceItemId, discountRate);
            return new ResponseEntity<>("Discount applied successfully.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while applying the discount.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/discount/global")
    public ResponseEntity<String> applyGlobalDiscount(
            @RequestParam Integer daysLeftTillExpire,
            @RequestParam Float discountRate) {
        try {
            discountService.applyGlobalDiscountBasedOnExpiration(daysLeftTillExpire, discountRate);
            return ResponseEntity.ok("Global discount applied successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error applying global discount: " + e.getMessage());
        }
    }
}
