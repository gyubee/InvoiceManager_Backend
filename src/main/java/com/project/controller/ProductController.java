package com.project.controller;

import com.project.dto.ItemDiscountDTO;
import com.project.dto.ProductExpiryDTO;
import com.project.entity.Product;
import com.project.service.InvoiceService;
import com.project.service.SalesService;
import com.project.service.DiscountService;
import com.project.service.InvoiceItemService;
import com.project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.math.BigDecimal;

@RestController
@RequestMapping("/invoicemanager/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private InvoiceItemService invoiceItemService;

    @Autowired
    private DiscountService discountService;

    @Operation(summary = "Get all products", description = "Retrieve a list of all products.")
    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> getProductById(
            @Parameter(description = "ID of the product to retrieve", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // STOCK
    @Operation(summary = "Get all products sorted by stock ascending", description = "Retrieve all products sorted by stock ascending.")
    @GetMapping("/stock")
    public List<Product> getAllProductsSortedByStock() {
        return productService.getAllProductsSortedByStock();
    }

    @Operation(summary = "Get all products with stock available", description = "Retrieve all products that have stock available(stock > 0).")
    @GetMapping("/all/available")
    public List<Product> getAllProductsWithStock() {
        return productService.getAllProductsWithStock();
    }

    @Operation(summary = "Get stock by product ID", description = "Retrieve the stock quantity for a specific product.")
    @GetMapping("/{productId}/stock")
    public ResponseEntity<Integer> getStockByProductId(
            @Parameter(description = "ID of the product to check stock for", example = "1")
            @PathVariable Integer productId) {
        return ResponseEntity.ok(productService.getStockByProductId(productId));
    }

    @Operation(summary = "Update product stock", description = "Update the stock quantity for a specific product.")
    @PutMapping("/{productId}/stock")
    public ResponseEntity<Product> updateProductStock(
            @Parameter(description = "ID of the product to update", example = "1")
            @PathVariable Integer productId,
            @Parameter(description = "New stock quantity", example = "50")
            @RequestParam Integer newStock) {
        return ResponseEntity.ok(productService.updateProductStock(productId, newStock));
    }


    // DISCOUNT
    @Operation(summary = "Get all products with closest expiry date first", description = "Retrieve a list of products with the closest expiry dates(not considering past expiration date).")
    @GetMapping("/expiry")
    public List<ProductExpiryDTO> getAllProductsWithEarliestExpiry() {
        return productService.getAllProductsWithEarliestExpiry();
    }

    @Operation(summary = "Get discount details by product ID", description = "Retrieve discount details needed for discount managing a specific product.")
    @GetMapping("/{productId}/expiry")
    public ResponseEntity<List<ItemDiscountDTO>> findDiscountDetailsByProductId(
            @Parameter(description = "ID of the product to retrieve discount details for", example = "1")
            @PathVariable Integer productId) {
        return ResponseEntity.ok(invoiceItemService.findDiscountDetailsByProductId(productId));
    }

    @Operation(summary = "Apply discount to an item", description = "Apply a discount to a specific item with certain expiry date.")
    @PostMapping("/{productId}/{invoiceItemId}/discounts/apply")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discount applied successfully."),
            @ApiResponse(responseCode = "404", description = "Product or invoice item not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<String> applyDiscount(
            @Parameter(description = "ID of the product", example = "1")
            @PathVariable Integer productId,
            @Parameter(description = "ID of the invoice item", example = "101")
            @PathVariable Integer invoiceItemId,
            @Parameter(description = "Discount rate to apply", example = "10.0")
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

    @Operation(summary = "Apply global discount based on expiry", description = "Apply a global discount to all products based on their expiration date(this will be added to all item products that apply and created in discount table).")
    @PostMapping("/discount/global")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Global discount applied successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<String> applyGlobalDiscount(
            @Parameter(description = "Number of days left until expiry", example = "10")
            @RequestParam Integer daysLeftTillExpire,
            @Parameter(description = "Discount rate to apply globally", example = "15.0")
            @RequestParam Float discountRate) {
        try {
            discountService.applyGlobalDiscountBasedOnExpiration(daysLeftTillExpire, discountRate);
            return ResponseEntity.ok("Global discount applied successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error applying global discount: " + e.getMessage());
        }
    }


    @Autowired
    private SalesService salesService;
    //1. month total margin
    //    http://localhost:8001/invoicemanager/products/monthly-revenue?salesMonth=2024-02-01
    @GetMapping("/monthly-revenue")
    public ResponseEntity<Map<String, Object>> getMonthlyRevenue(@RequestParam String salesMonth) {
        LocalDate date = LocalDate.parse(salesMonth); // 쿼리 패러미터로 변경

        BigDecimal revenue = salesService.getTotalRevenueForMonth(date);

        Map<String, Object> response = new HashMap<>();
        response.put("year", date.getYear());
        response.put("month", date.getMonth());
        response.put("totalRevenue", revenue);

        return ResponseEntity.ok(response);
    }



    //   2. bestseller



    @GetMapping("/bestsellers")
    public ResponseEntity<List<Map<String, Object>>> getBestsellers() {
        List<Map<String, Object>> bestsellers = salesService.getBestSellers();
        return ResponseEntity.ok(bestsellers);
    }

    //3. positive trending
    // 전월 대비 판매량 상승 비율 상위 5개 항목을 리턴하는 API
    @GetMapping("/trending/positive")
    public ResponseEntity<List<Map<String, Object>>> getTopPositive(@RequestParam String salesMonth) {
        LocalDateTime date = LocalDate.parse(salesMonth).atStartOfDay();  // 입력된 날짜를 LocalDate로 변환

        // 서비스 계층에서 비즈니스 로직 처리
        List<Map<String, Object>> topPositive = salesService.calculatePositiveTrending(date);

        return ResponseEntity.ok(topPositive);
    }

    //4. positive trending
    // 전월 대비 판매량 상승 비율 상위 5개 항목을 리턴하는 API
    @GetMapping("/trending/negative")
    public ResponseEntity<List<Map<String, Object>>> getTopNegative(@RequestParam String salesMonth) {
        LocalDateTime date = LocalDate.parse(salesMonth).atStartOfDay();  // 입력된 날짜를 LocalDate로 변환

        // 서비스 계층에서 비즈니스 로직 처리
        List<Map<String, Object>> topNegative = salesService.calculateNegativeTrending(date);

        return ResponseEntity.ok(topNegative);
    }

//    5. category
//    http://localhost:8001/invoicemanager/products/category-percentage?salesMonth=2024-02-01


    @GetMapping("/category-percentage")
    public ResponseEntity<Map<String, Double>> getCategorySalesPercentage(@RequestParam String salesMonth) {
        LocalDateTime salesMonthDateTime = LocalDate.parse(salesMonth).atStartOfDay();
        Map<String, Double> categorySalesPercentage = salesService.getCategorySalesPercentage(salesMonthDateTime);
        return ResponseEntity.ok(categorySalesPercentage);
    }


//    test
//    @GetMapping("/bestsellers")
//    public String getBestsellers() {
//        return "AAAA";
//    }



}
